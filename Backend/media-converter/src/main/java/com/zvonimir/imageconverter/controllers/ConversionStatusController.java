package com.zvonimir.imageconverter.controllers;

import com.zvonimir.imageconverter.models.ConversionResult;
import com.zvonimir.imageconverter.services.ConversionJobService;
import com.zvonimir.imageconverter.services.JobTrackingService;
import com.zvonimir.imageconverter.util.FilenameUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/status")
@CrossOrigin(origins = "*")
public class ConversionStatusController {

    private final JobTrackingService jobTrackingService;

    @Autowired
    public ConversionStatusController(JobTrackingService jobTrackingService) {
        this.jobTrackingService = jobTrackingService;
    }

    @Operation(summary = "Check progress of a conversion job by jobId")
    @GetMapping("/progress/{jobId}")
    public ResponseEntity<?> getProgress(@PathVariable String jobId) {
        if (!jobTrackingService.hasJob(jobId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");
        }

        int progress = jobTrackingService.getProgress(jobId);

        if (progress == -1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("jobId", jobId, "status", "error"));
        }

        return ResponseEntity.ok(Map.of("jobId", jobId, "progress", progress));
    }

    @Operation(summary = "Download the result of a completed conversion")
    @GetMapping("/result/{jobId}")
    public ResponseEntity<?> downloadImage(@PathVariable String jobId) {
        if (!jobTrackingService.hasJob(jobId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");
        }

        ConversionResult result = jobTrackingService.getResult(jobId);

        if (result.getData() == null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Conversion not yet complete. Try again later.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(
                ContentDisposition.attachment().filename(FilenameUtils.generateConvertedFilename(result.getOldFilename(), result.getFormat())).build()
        );
        jobTrackingService.removeJob(jobId);

        return ResponseEntity.ok().headers(headers).body(result.getData());
    }
}
