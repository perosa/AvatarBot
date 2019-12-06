package com.perosa.avatarbot.core.model;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionStore {

    private static Map<String, Session> map = new HashMap<>();

    public void addTo(Session session) {
        map.put(session.getId(), session);
    }

    public Session getFrom(String id) {
        return map.get(id);
    }

    public Session removeFrom(String id) {
        return map.remove(id);
    }

    public void clear() {
        map.clear();
    }

}
