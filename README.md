# Media Converter (images and videos)

A full-stack multimedia converter web application built with:

- **Angular 17** (standalone) — frontend  
- **Spring Boot** — backend  
- **FFmpeg** — media processing

Supports converting **images** and **videos** to multiple formats with file upload, progress indication, and automatic download.

---

## How to Run

### 🛠 Backend (Spring Boot)

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

## ⚙ Backend Architecture

To simulate realistic conversion progress and avoid blocking the request thread, the backend uses multithreading for media conversion jobs.

- Each conversion runs in a **separate thread** using `new Thread(...)` from `ConversionJobService`
- This allows the frontend to poll `/api/status/progress/{jobId}` for live progress updates
- The job is tracked using `JobTrackingService` and a unique `jobId`
- Converted data is returned as a byte array and downloaded automatically on the frontend

This design ensures a responsive user experience and scalable backend processing.
---

## ⚠️ Notes

- Max file size: **10 MB**
- For image converting progress is simulated on backend
- Large binaries (e.g. FFmpeg) should be excluded from Git or managed with Git LFS

---

