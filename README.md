# Media Converter

A full-stack multimedia converter web application built with:

- Angular 17 (standalone) — frontend  
- Spring Boot — backend  
- FFmpeg — media processing

Supports converting images and videos to multiple formats with file upload, progress indication, and automatic download.

---

## How to Run

### Backend (Spring Boot)

```bash
cd Backend/media-converter
./mvnw spring-boot:run
```

Runs at: `http://localhost:8080`

Make sure `ffmpeg.exe`, `ffplay.exe`, and `ffprobe.exe` are available under `external/ffmpeg/`.

---

### Frontend (Angular 17)

```bash
cd Frontend/multimedia-converter
npm install
ng serve
```

Runs at: `http://localhost:4200`

---

## API Endpoints

| Method | Endpoint                          | Description                    |
|--------|-----------------------------------|--------------------------------|
| POST   | `/api/image/convert`             | Start image conversion         |
| POST   | `/api/video/convert`             | Start video conversion         |
| GET    | `/api/status/progress/{jobId}`   | Get conversion progress        |
| GET    | `/api/status/result/{jobId}`     | Download converted file        |

---

## Backend Architecture and Design

### Strategy Pattern for Conversion

The backend uses the Strategy Pattern to handle different media types:

- `MediaConversionStrategy<T extends FormatType>` defines a generic interface
- `ImageConverterStrategy` and `VideoConverterStrategy` implement format-specific logic
- `MediaConversionContext` delegates to the appropriate strategy at runtime


---

### Asynchronous Job Handling with Progress Tracking

To simulate realistic conversion progress and avoid blocking the request thread, the backend uses multithreading for media conversion jobs:

- Each conversion runs in a separate thread using `new Thread(...)` from `ConversionJobService`
- This allows the frontend to poll `/api/status/progress/{jobId}` for live progress updates
- The job is tracked using `JobTrackingService` and a unique `jobId`
- Converted data is returned as a byte array and downloaded automatically on the frontend

---

### Job Flow Summary

1. The frontend uploads a file with the selected target format
2. The backend saves the file and starts a conversion thread
3. A specific strategy is chosen depending on the media type
4. `JobTrackingService` monitors progress and stores results
5. The frontend polls progress using `/status/progress/{jobId}`
6. Once finished, the frontend downloads the result via `/status/result/{jobId}`

---

## Notes

- Maximum upload file size is 10 MB
- Image conversion progress is simulated
- Video conversion progress is parsed from FFmpeg output

---


