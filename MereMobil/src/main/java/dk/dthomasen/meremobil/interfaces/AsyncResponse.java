package dk.dthomasen.meremobil.interfaces;

import net.bican.wordpress.Page;

import java.util.List;

public interface AsyncResponse {
    void processFinish(List<Page> output);
}
