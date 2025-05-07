package com.zvonimir.imageconverter.controllers;

import com.zvonimir.imageconverter.models.ImageFormat;
import com.zvonimir.imageconverter.models.ConversionResult;
import com.zvonimir.imageconverter.services.ConversionJobService;
import com.zvonimir.imageconverter.util.FilenameUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/image")
@CrossOrigin(origins = "*")
public class ImageConverterController {

    private final ConversionJobService jobService;

    @Autowired
    public ImageConverterController(ConversionJobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "Start an image conversion job and receive jobId")
    @PostMapping(value = "/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> startConversion(
            @Parameter(description = "Image file (max 10MB)")
            @RequestParam("file") MultipartFile file,

            @Parameter(
                    description = "Target image format (e.g. PNG, JPEG, BMP, GIF)",
                    required = true,
                    schema = @Schema(implementation = ImageFormat.class)
            )
            @RequestParam("format") ImageFormat format) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }

        String jobId = jobService.startJob(file, format);

        return ResponseEntity.ok(Map.of(
                "jobId", jobId,
                "message", "Conversion started. Use /api/convert/status/{jobId} to track progress."
        ));
    }
}
