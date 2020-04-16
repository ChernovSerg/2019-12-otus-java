package ru.otus.chernovsa.jdbc.dao.mapper;

import java.util.HashMap;
import java.util.Map;

public class SqlRequests {
    private Map<SqlRequestType, String> requests = new HashMap<>();

    public void put(SqlRequestType requestType, String request) {
        requests.put(requestType, request);
    }

    public String getRequest(SqlRequestType requestType) {
        return requests.get(requestType);
    }

    public boolean hasRequest(SqlRequestType requestType) {
        for (SqlRequestType sqlRequestType : requests.keySet()) {
            if (sqlRequestType.equals(requestType)) {
                return true;
            }
        }
        return false;
    }

}
