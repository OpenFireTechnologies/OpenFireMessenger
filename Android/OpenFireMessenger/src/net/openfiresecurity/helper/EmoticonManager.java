package net.openfiresecurity.helper;

import android.content.Context;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.Spanned;
import android.text.style.ImageSpan;

import net.openfiresecurity.messenger.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmoticonManager {
    private static final Factory spannableFactory = Spannable.Factory
            .getInstance();

    private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

    static {
        /* MEME */
        addPattern(emoticons, ":bitchplease:", R.drawable.meme_dumb);
        addPattern(emoticons, ":falol:", R.drawable.meme_falol);
        addPattern(emoticons, ":foreveralone:", R.drawable.meme_foreveralone);
        addPattern(emoticons, ":fu:", R.drawable.meme_fu);
        addPattern(emoticons, ":happy:", R.drawable.meme_happycry);
        addPattern(emoticons, ":likeaboss:", R.drawable.meme_likeaboss);
        addPattern(emoticons, ":lol:", R.drawable.meme_lol);
        addPattern(emoticons, ":lulz:", R.drawable.meme_lulz);
        addPattern(emoticons, ":megusta:", R.drawable.meme_megusta);
        addPattern(emoticons, ":troll:", R.drawable.meme_troll);
        addPattern(emoticons, ":trolol:", R.drawable.meme_trolol);
        addPattern(emoticons, ":yuno:", R.drawable.meme_yuno);

    }

    private static void addPattern(Map<Pattern, Integer> map, String smile,
                                   int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    private static void addSmiles(final Context context,
                                  final Spannable spannable) {
        new Thread() {
            @Override
            public void run() {
                for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
                    Matcher matcher = entry.getKey().matcher(spannable);
                    while (matcher.find()) {
                        boolean set = true;
                        for (ImageSpan span : spannable
                                .getSpans(matcher.start(), matcher.end(),
                                        ImageSpan.class)) {
                            if ((spannable.getSpanStart(span) >= matcher
                                    .start())
                                    && (spannable.getSpanEnd(span) <= matcher
                                    .end())) {
                                spannable.removeSpan(span);
                            } else {
                                set = false;
                                break;
                            }
                        }
                        if (set) {
                            spannable.setSpan(
                                    new ImageSpan(context, entry.getValue()),
                                    matcher.start(), matcher.end(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
        }.run();
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }
}
