package com.vector.onboarding.domain.dataview.service;

import com.vector.onboarding.domain.dataview.entity.DataView;
import com.vector.onboarding.domain.dataview.entity.SchemaAnalysisResult;
import com.vector.onboarding.domain.dataview.repository.DataViewRepository;
import com.vector.onboarding.domain.dataview.repository.SchemaAnalysisResultRepository;
import com.vector.onboarding.domain.space.Space;
import com.vector.onboarding.domain.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataViewService {

    private final DataViewRepository dataViewRepository;
    private final SchemaAnalysisResultRepository schemaAnalysisResultRepository;
    private final GithubFileFetchService githubFileFetchService;
    private final SchemaParserService schemaParserService;
    private final SpaceRepository spaceRepository;

    @Transactional
    public String getOrAnalyzeSchema(Long spaceId) {
        // 0. 스페이스 레포지토리 URL 가져오기
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("스페이스를 찾을 수 없습니다."));
        String repositoryUrl = space.getRepoUrl();

        // 1. DataView DB에서 해당 스페이스(팀)의 데이터 조회
        List<DataView> dataViews = dataViewRepository.findAllBySpaceId(spaceId);
        
        if (dataViews.isEmpty()) {
            log.warn("DB에 데이터(DataView)가 없습니다. spaceId: {}, repositoryUrl: {}", spaceId, repositoryUrl);
            return "{ \"nodes\": [], \"edges\": [] }";
        }

        // 2. 캐시 확인 (spaceId 기준으로 확인)
        Optional<SchemaAnalysisResult> cachedResult = schemaAnalysisResultRepository.findBySpaceId(spaceId);
        
        if (cachedResult.isPresent()) {
            SchemaAnalysisResult result = cachedResult.get();
            String cachedJson = result.getAnalyzedJson();
            
            // 캐시가 비어있거나 유효한 데이터(노드)가 없는 경우 무시하고 새로 분석하도록 처리
            boolean isEmptyCache = cachedJson == null || 
                                   cachedJson.trim().isEmpty() || 
                                   cachedJson.contains("\"nodes\":[]") || 
                                   cachedJson.contains("\"nodes\": []");
                                   
            if (!isEmptyCache) {
                log.info("캐시 히트: 기존 분석 데이터를 반환합니다. spaceId: {}", spaceId);
                return cachedJson;
            } else {
                log.info("캐시가 비어있어(이전 오류 결과) 무시하고 새로 분석합니다. spaceId: {}", spaceId);
            }
        }

        // 3. 새로운 데이터 감지됨 (또는 캐시 없음). 파일 내용 Github API로 가져오기
        log.info("캐시 없음. Github API를 통해 파일 내용을 가져옵니다. spaceId: {}", spaceId);
        String combinedFileContents = dataViews.stream()
                .map(dataView -> {
                    String filePath = dataView.getFilePath();
                    
                    if (filePath == null || filePath.isEmpty()) {
                        log.warn("DataView 항목에 file_path가 없습니다. ID: {}", dataView.getId());
                        return "";
                    }
                    
                    String content = githubFileFetchService.fetchFileContent(repositoryUrl, filePath);
                    return "--- File: " + filePath + " ---\n" + content + "\n";
                })
                .filter(content -> !content.isEmpty())
                .collect(Collectors.joining("\n"));

        // 4. 정규식 파서를 통한 전체 병합 파싱
        log.info("Java 정규식 기반 스키마 분석을 시작합니다. spaceId: {}", spaceId);
        String analyzedJson = schemaParserService.parseSchema(combinedFileContents);

        // 5. 결과를 DB에 저장/업데이트
        String dummyCommitHash = "fetched-at-" + System.currentTimeMillis();
        if (cachedResult.isPresent()) {
            SchemaAnalysisResult result = cachedResult.get();
            result.updateAnalysis(dummyCommitHash, analyzedJson);
        } else {
            SchemaAnalysisResult newResult = SchemaAnalysisResult.builder()
                    .spaceId(spaceId)
                    .repositoryUrl(repositoryUrl)
                    .commitHash(dummyCommitHash) // Commit hash is no longer the main cache key, but required by entity
                    .analyzedJson(analyzedJson)
                    .build();
            schemaAnalysisResultRepository.save(newResult);
        }

        return analyzedJson;
    }
}
