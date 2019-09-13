package com.grouter;

/**
 * 用于响应错误
 */
class ErrorTask extends GRouterTask {
    private Exception exception;

    ErrorTask(Exception exception) {
        this.exception = exception;
    }

    @Override
    public Object process() throws Exception {
        throw exception;
    }
}
