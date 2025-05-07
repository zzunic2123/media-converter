# Media Converter (images and videos)

A full-stack multimedia converter web application built with:

- **Angular 17** (standalone) — frontend  
- **Spring Boot** — backend  
- **FFmpeg** — media processing

Supports converting **images** and **videos** to multiple formats with file upload, progress indication, and automatic download.

---

## How to Run

### Backend (Spring Boot)

```bash
cd Backend/media-converter
./mvnw spring-boot:run
```

Runs at: `http://localhost:8080`

> Ensure `ffmpeg.exe`, `ffplay.exe`, and `ffprobe.exe` are available under `external/ffmpeg/`.

---

### Frontend (Angular 17)

```bash
cd Frontend/multimedia-converter
npm install
ng serve
```

Runs at: `http://localhost:4200`

---


##  API Endpoints

| Method | Endpoint                          | Description                    |
|--------|-----------------------------------|--------------------------------|
| POST   | `/api/image/convert`             | Start image conversion         |
| POST   | `/api/video/convert`             | Start video conversion         |
| GET    | `/api/status/progress/{jobId}`   | Get conversion progress        |
| GET    | `/api/status/result/{jobId}`     | Download converted file        |

---

## Backend Architecture & Design

### Strategy Pattern for Conversion

The backend uses the **Strategy Pattern** to handle different media types:

- `MediaConversionStrategy<T extends FormatType>` defines a generic interface
- `ImageConverterStrategy` and `VideoConverterStrategy` implement format-specific logic
- `MediaConversionContext` delegates to the appropriate strategy at runtime

This makes it easy to extend the system with more formats or conversion methods later.

### Asynchronous Job Handling

To allow progress polling and avoid blocking the request thread:

- `ConversionJobService.startJob(...)` saves the file and spawns a **new thread**
- Conversion is done in background (r via FFmpeg)
- Progress is tracked and updated via `JobTrackingService`
- Once complete, the converted result is stored and ready for download

### Job Flow Summary

1. **Frontend** uploads a file with target format
2. **Backend** saves the file and starts a conversion thread
3. **Strategy** is selected based on the file type (image or video)
4. **JobTrackingService** tracks and updates progress
5. **Frontend** polls `/status/progress/{jobId}`
6. Once done, the frontend calls `/status/result/{jobId}` to download the file

##  Notes

- Max file size: **10 MB**
- For image converting progress is simulated on backend
- Large binaries (e.g. FFmpeg) should be excluded from Git or managed with Git LFS

---

