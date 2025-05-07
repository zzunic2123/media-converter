import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import {ImageConverterComponent} from "./components/image-converter/image-converter.component";
import {VideoConverterComponent} from "./components/video-converter/video-converter.component";

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'image-converter', component: ImageConverterComponent },
  { path: 'video-converter', component: VideoConverterComponent },
  { path: '**', redirectTo: '' }
];
