import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {VideosListComponent} from './components/videos-list/videos-list.component';
import {VideoDetailsComponent} from './components/videos-list/video-details/video-details.component';
import {VideoUploadComponent} from './components/video-upload/video-upload.component';
import {RegisterUserComponent} from './components/register-user/register-user.component';
import {KeycloakAuthGuardService} from './services/keycloak/keycloak-auth-guard.service';


const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [KeycloakAuthGuardService] },
  { path: 'videos', component: VideosListComponent, canActivate: [KeycloakAuthGuardService] },
  { path: 'videos/upload', component: VideoUploadComponent, canActivate: [KeycloakAuthGuardService] },
  { path: 'videos/:id', component: VideoDetailsComponent, canActivate: [KeycloakAuthGuardService] },
  { path: 'signup', component: RegisterUserComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
