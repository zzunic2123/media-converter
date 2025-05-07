import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // required for *ngIf
import { ImageConverterComponent } from '../image-converter/image-converter.component';
import { VideoConverterComponent } from '../video-converter/video-converter.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, ImageConverterComponent, VideoConverterComponent,RouterLink ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  selected: 'image' | 'video' | null = null;
}
