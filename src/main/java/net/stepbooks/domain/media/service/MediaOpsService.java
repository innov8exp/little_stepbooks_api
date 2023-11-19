package net.stepbooks.domain.media.service;

import net.stepbooks.domain.media.entity.Media;

import java.util.List;

public interface MediaOpsService {
    List<Media> getByIds(String[] ids);
}
