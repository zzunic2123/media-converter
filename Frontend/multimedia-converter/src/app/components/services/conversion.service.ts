import { Injectable } from '@angular/core';
import { HttpClient, HttpEventType, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, interval, switchMap, takeWhile, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ConversionService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  startImageConversion(file: File, format: string): Observable<{ jobId: string }> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('format', format);

    return this.http.post<{ jobId: string }>(`${this.apiUrl}/image/convert`, formData);
  }

  startVideoConversion(file: File, format: string): Observable<{ jobId: string }> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('format', format);

    return this.http.post<{ jobId: string }>(`${this.apiUrl}/video/convert`, formData);
  }


  getJobProgress(jobId: string): Observable<number> {
    return this.http.get<{ jobId: string; progress: number }>(`${this.apiUrl}/status/progress/${jobId}`)
      .pipe(
        switchMap(response => {
          return new Observable<number>(observer => {
            observer.next(response.progress);
            observer.complete();
          });
        })
      );
  }

  pollUntilDone(jobId: string, intervalMs: number = 500): Observable<number> {
    return interval(intervalMs).pipe(
      switchMap(() => this.getJobProgress(jobId)),
      takeWhile(progress => progress < 100, true) // true = emit 100 too
    );
  }

  downloadResult(jobId: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/status/result/${jobId}`, {
      responseType: 'blob',
      observe: 'response'
    }).pipe(
      switchMap((response: HttpResponse<Blob>) => {
        if (response.status === 202) {
          return throwError(() => new Error('Conversion not complete.'));
        }

        return new Observable<Blob>(observer => {
          observer.next(response.body!);
          observer.complete();
        });
      })
    );
  }
}
