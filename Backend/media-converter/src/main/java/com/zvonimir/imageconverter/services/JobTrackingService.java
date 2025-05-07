package com.zvonimir.imageconverter.services;

import com.zvonimir.imageconverter.models.ConversionResult;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JobTrackingService {

    private final Map<String, Integer> progressMap = new ConcurrentHashMap<>();
    private final Map<String, ConversionResult> resultMap = new ConcurrentHashMap<>();

    public void updateProgress(String jobId, int progress) {
        progressMap.put(jobId, progress);
    }

    public Integer getProgress(String jobId) {
        return progressMap.get(jobId);
    }

    public void storeResult(String jobId, ConversionResult result) {
        resultMap.put(jobId, result);
    }

    public ConversionResult getResult(String jobId) {
        return resultMap.get(jobId);
    }

    public boolean hasJob(String jobId) {
        return progressMap.containsKey(jobId);
    }

    public void markFailed(String jobId) {
        progressMap.put(jobId, -1);
    }

    public void removeJob(String jobId) {
        progressMap.remove(jobId);
        resultMap.remove(jobId);
    }
}
