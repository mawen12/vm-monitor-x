package com.github.mawen12.easeagent.api.trace;

public interface TraceConst {
    String HTTP_ATTRIBUTE_ROUTER = "http.route";
    String HTTP_ATTRIBUTE_ERROR = "error";

    String TAG_HTTP_ROUTE = HTTP_ATTRIBUTE_ROUTER;
    String TAG_HTTP_METHOD = "http.method";
    String TAG_HTTP_PATH = "http.path";
    String TAG_STATUS_CODE = "http.status_code";
    String TAG_STATUS_ERR = "error";
}
