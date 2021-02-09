export interface Options {
    useWorkerThread?: boolean;
}
export interface SearchOptions extends Options {
    type?: "video" | "channel" | "playlist" | "all";
    limit?: number;
    page?: number;
}
export interface GetRelatedOptions extends Options {
    limit?: number;
}
export interface SearchResult {
    id: string;
    type?: "video" | "channel" | "playlist";
}
export interface Video extends SearchResult {
    title: string;
    duration: number | null;
    thumbnail: string;
    channel: Channel;
    uploadDate: string;
    viewCount: number | null;
}
export interface VideoDetailed {
    id: string;
    title: string;
    duration: number | null;
    thumbnail: string;
    description: string;
    channel: Channel;
    uploadDate: string;
    viewCount: number | null;
    likeCount: number | null;
    dislikeCount: number | null;
    isLiveContent: boolean;
    tags: string[];
}
export interface Playlist extends SearchResult {
    title: string;
    thumbnail: string;
    channel: Channel;
    videoCount: number;
}
export interface PlaylistDetailed {
    id: string;
    title: string;
    videoCount: number;
    viewCount: number;
    lastUpdatedAt: string;
    channel?: Channel;
    videos: Video[];
}
export interface Channel extends SearchResult {
    name: string;
    url: string;
    thumbnail?: string;
    videoCount?: number;
}
export declare type SearchType<T> = T extends {
    type: "video";
} ? Video : T extends {
    type: "channel";
} ? Channel : T extends {
    type: "playlist";
} ? Playlist : Video | Channel | Playlist;
