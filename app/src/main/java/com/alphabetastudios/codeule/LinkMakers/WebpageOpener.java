package com.alphabetastudios.codeule.LinkMakers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by abheisenberg on 28/7/17.
 */

public class WebpageOpener {
    String hhlink;
    Intent startLink;
    Context context;

    public WebpageOpener(Context context, String link){
        this.context = context;
        this.hhlink = link;

        startLink = new Intent();
        startLink.setAction(Intent.ACTION_VIEW);
        startLink.addCategory(Intent.CATEGORY_BROWSABLE);
        startLink.setData(Uri.parse(hhlink));
    }

    public void LaunchLink() {
        context.startActivity(startLink);
    }

}
