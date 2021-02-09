import { Video, VideoDetailed, Playlist, PlaylistDetailed, Channel, SearchOptions } from "./types";
/**
 * Scrape search result from passed HTML
 *
 * @param html HTML
 * @param options Search options
 */
export declare function parseSearch(html: string, options: SearchOptions): (Video | Playlist | Channel)[];
/**
 * Scrape playlist result from passed HTML
 *
 * @param html HTML
 */
export declare function parseGetPlaylist(html: string): PlaylistDetailed | {};
/**
 * Scrape video result from passed HTML
 *
 * @param html HTML
 */
export declare function parseGetVideo(html: string): VideoDetailed | {};
/**
 * Scrape related video from a video from passed HTML
 *
 * @param html HTML
 */
export declare function parseGetRelated(html: string, limit: number): Video[];
/**
 * Scrape up next video from a video from passed HTML
 *
 * @param html HTML
 */
export declare function parseGetUpNext(html: string): Video | {};
declare const _default: {
    parseSearch: typeof parseSearch;
    parseGetPlaylist: typeof parseGetPlaylist;
    parseGetVideo: typeof parseGetVideo;
    parseGetRelated: typeof parseGetRelated;
    parseGetUpNext: typeof parseGetUpNext;
};
export default _default;
