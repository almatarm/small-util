/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smallutil.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.io.TextEncoding;
import org.blinkenlights.jid3.v2.APICID3V2Frame;
import org.blinkenlights.jid3.v2.ID3V2_3_0Tag;
import org.blinkenlights.jid3.v2.USLTID3V2Frame;

/**
 *
 * @author MUFEED
 */
public class MP3Util {

    public static void updateTag(String mp3file, int track, String title, String artist,
            String album, String genre, String comment, int year,
            APICID3V2Frame picFrame) throws ID3Exception {
        updateTag(mp3file, track, title, artist, album, genre, comment, year, picFrame, null);
    }

    public static void updateTag(String mp3file, int track, String title, String artist,
            String album, String genre, String comment, int year,
            APICID3V2Frame picFrame, USLTID3V2Frame lyricsFrame) throws ID3Exception {
        
        TextEncoding.setDefaultTextEncoding(TextEncoding.UNICODE);

        //create an MP3File object representing our chosen file
        MediaFile mdFile = new MP3File(new File(mp3file));

        // create a v2.3.0 tag object, and set values using convenience methods
        ID3V2_3_0Tag tag = new ID3V2_3_0Tag();
        tag.setAlbum(album); // sets TALB frame
        tag.setArtist(artist); // sets TPE1 frame
        tag.setComment(comment); // sets COMM frame with language "eng" and no description
        tag.setGenre(genre); // sets TCON frame
        tag.setTitle(title); // sets TIT2 frame
        tag.setYear(year); // sets TYER frame
        tag.setTrackNumber(track);
        if(picFrame != null)
            tag.addAPICFrame(picFrame);
        if(lyricsFrame != null)
            tag.addUSLTFrame(lyricsFrame);
        
        // set this v2.3.0 tag in the media file object
        mdFile.setID3Tag(tag);
        // update the actual file to reflect the current state of our object
        mdFile.sync();
    }

    public static APICID3V2Frame getPicframe(String imagePath) throws IOException, ID3Exception {
        File image = new File(imagePath);
        InputStream s = new FileInputStream(image);
        FileInputStream in = new FileInputStream(image);
        FileChannel fc = in.getChannel();
        byte[] data = new byte[(int) fc.size()];   // fc.size returns the size of the file which backs the channel
        ByteBuffer bb = ByteBuffer.wrap(data);
        fc.read(bb);

        String imageType = image.getName().substring(image.getName().lastIndexOf(".") + 1);
        String mimeType = imageType.equalsIgnoreCase("jpg") ? "image/jpeg" : "image/" + imageType;

        return new APICID3V2Frame(mimeType, APICID3V2Frame.PictureType.FrontCover, "Frontcover", data);
    }

    public static USLTID3V2Frame getLyricsframe(String lyrics) throws IOException, ID3Exception {
//        InputStream in = new ByteArrayInputStream(lyrics.getBytes("UTF-8"));
//
//        USLTID3V2Frame lyricsFrame = new USLTID3V2Frame(in);
//        lyricsFrame.setLanguage("ARA");
//        lyricsFrame.setTextEncoding(TextEncoding.UNICODE);
        USLTID3V2Frame lyricsFrame = new USLTID3V2Frame("ARA", "Lyrics", lyrics);
        lyricsFrame.setTextEncoding(TextEncoding.UNICODE);
        return lyricsFrame;
    }

}
