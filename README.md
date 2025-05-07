# 🎞️ Media Converter

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

## ⚠️ Notes

- Max file size: **10 MB**
- Progress is frontend-simulated
- Large binaries (e.g. FFmpeg) should be excluded from Git or managed with Git LFS

---

