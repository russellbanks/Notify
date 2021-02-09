import { Video, VideoDetailed, PlaylistDetailed, Options, SearchOptions, SearchType, GetRelatedOptions } from "./common/types";
export * from "./common/types";
/**
 * Search youtube for a list of  based on a search query.
 *
 * @param query Search Query
 * @param options Options for scraper
 */
export declare const search: <T extends SearchOptions>(query: string, options?: Partial<T>) => Promise<SearchType<T>[]>;
/**
 * Search youtube for playlist information.
 *
 * @param playlistId Id of the playlist
 * @param options Options for scraper
 */
export declare const getPlaylist: (playlistId: string, options?: Options) => Promise<PlaylistDetailed | {}>;
/**
 * Search youtube for video information.
 *
 * @param videoId Id of the video
 * @param options Options for scraper
 */
export declare const getVideo: (videoId: string, options?: Options) => Promise<VideoDetailed | {}>;
/**
 * Search youtube for related videos based on videoId.
 *
 * @param videoId Id of the video
 * @param options Options for scraper
 */
export declare const getRelated: (videoId: string, options?: GetRelatedOptions) => Promise<Video[]>;
/**
 * Search youtube for up next video based on videoId.
 *
 * @param videoId Id of the video
 * @param options Options for scraper
 */
export declare const getUpNext: (videoId: string, options?: Options) => Promise<Video | {}>;
declare const _default: {
    search: <T extends SearchOptions>(query: string, options?: Partial<T>) => Promise<SearchType<T>[]>;
    getPlaylist: (playlistId: string, options?: Options) => Promise<{} | PlaylistDetailed>;
    getVideo: (videoId: string, options?: Options) => Promise<{} | VideoDetailed>;
    getRelated: (videoId: string, options?: GetRelatedOptions) => Promise<Video[]>;
    getUpNext: (videoId: string, options?: Options) => Promise<{} | Video>;
};
export default _default;
