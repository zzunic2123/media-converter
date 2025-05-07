package com.zvonimir.imageconverter.controllers;

import com.zvonimir.imageconverter.models.VideoFormat;
import com.zvonimir.imageconverter.services.ConversionJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/video")
@CrossOrigin(origins = "*")
public class VideoConverterController {

    private final ConversionJobService jobService;

    @Autowired
    public VideoConverterController(ConversionJobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "Start a video conversion job and receive jobId")
    @PostMapping(value = "/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> startVideoConversion(
            @Parameter(description = "Video file (e.g. .mp4, .avi)", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(
                    description = "Target video format (e.g. MP4, AVI, MKV)",
                    required = true,
                    schema = @Schema(implementation = VideoFormat.class)
            )
            @RequestParam("format") VideoFormat format) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No video file uploaded.");
        }

        String jobId = jobService.startJob(file, format);

        return ResponseEntity.ok(Map.of(
                "jobId", jobId,
                "message", "Video conversion started. Use /api/convert/status/{jobId} to track progress."
        ));
    }
}
