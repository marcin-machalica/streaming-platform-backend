import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {environment} from '../../../../environments/environment';
import {Observable} from 'rxjs';
import {VideoRepresentation} from '../video/VideoDto';
import {ChannelAbout, ChannelUpdate} from './ChannelDto';

type ChannelAboutResponseType = HttpResponse<ChannelAbout>;
type VideoRepresentationArrayResponseType = HttpResponse<VideoRepresentation[]>;

@Injectable({
  providedIn: 'root'
})
export class ChannelService {

  resourceUrl = `${environment.serverUrl}api/v1/channels`;

  constructor(private http: HttpClient) { }

  getChannelAbout(channelName: string): Observable<ChannelAboutResponseType> {
    return this.http.get<ChannelAbout>(this.resourceUrl, { observe: 'response' });
  }

  getChannelVideos(channelName: string): Observable<VideoRepresentationArrayResponseType> {
    return this.http.get<VideoRepresentation[]>(`${this.resourceUrl}/${channelName}/videos`, { observe: 'response' });
  }

  createChannel(channelUpdate: ChannelUpdate): Observable<ChannelAboutResponseType> {
    return this.http.post<ChannelAbout>(this.resourceUrl, channelUpdate, { observe: 'response' });
  }

  updateChannel(channelUpdate: ChannelUpdate, channelName: string): Observable<ChannelAboutResponseType> {
    return this.http.put<ChannelAbout>(`${this.resourceUrl}/${channelName}`, channelUpdate, { observe: 'response' });
  }

  deleteChannel(channelName: string): Observable<HttpResponse<void>> {
    return this.http.delete<void>(`${this.resourceUrl}/${channelName}`, { observe: 'response' });
  }
}
