package org.fdroid.fdroid.net.bluetooth.httpish.headers;

import org.fdroid.fdroid.net.bluetooth.FileDetails;

public abstract class Header {

    private static Header[] VALID_HEADERS = {
        new ContentLengthHeader(),
        new ETagHeader(),
    };

    protected abstract String getName();
    protected abstract void handle(FileDetails details, String value);

    public static void process(FileDetails details, String header, String value) {
        header = header.toLowerCase();
        for (Header potentialHeader : VALID_HEADERS) {
            if (potentialHeader.getName().equals(header)) {
                potentialHeader.handle(details, value);
                break;
            }
        }
    }

}