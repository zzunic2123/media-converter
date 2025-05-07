import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {ConversionService} from "../services/conversion.service";
import {ProgressBarComponent} from "../progress-bar/progress-bar.component";

@Component({
  selector: 'app-image-converter',
  standalone: true,
  imports: [CommonModule, FormsModule, ProgressBarComponent],
  templateUrl: './image-converter.component.html',
  styleUrls: ['./image-converter.component.css']
})
export class ImageConverterComponent {
  selectedFormat: string = 'PNG';
  uploadedFile: File | null = null;
  jobId: string | null = null;
  progress: number = 0;
  isDownloading = false;

  imageFormats = ['PNG', 'JPEG', 'BMP', 'GIF'];

  constructor(private conversionService: ConversionService) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.uploadedFile = input.files[0];
    }
  }

  convert(): void {
    if (!this.uploadedFile) {
      alert('Please upload a file.');
      return;
    }

    this.conversionService.startImageConversion(this.uploadedFile, this.selectedFormat)
      .subscribe({
        next: ({ jobId }) => {
          this.jobId = jobId;
          console.log('Conversion started with job ID:', jobId);
          this.pollProgress(jobId);
        },
        error: err => {
          alert('Failed to start conversion: ' + err.message);
        }
      });
  }

  pollProgress(jobId: string): void {
    this.conversionService.pollUntilDone(jobId).subscribe({
      next: progress => {
        this.progress = progress;
        if (progress === 100) {
          this.download(jobId);
        }
      },
      error: err => alert('Error checking progress: ' + err.message)
    });
  }

  download(jobId: string): void {
    this.isDownloading = true;
    this.conversionService.downloadResult(jobId).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `converted.${this.selectedFormat.toLowerCase()}`;
        a.click();
        window.URL.revokeObjectURL(url);
        this.isDownloading = false;
      },
      error: err => {
        this.isDownloading = false;
        alert('Download failed: ' + err.message);
      }
    });
  }
}
