package com.grouter;

/**
 * 用于响应错误
 */
class MockTask extends GRouterTask {
    private Object result;

    public MockTask(Object result) {
        this.result = result;
    }

    @Override
    public Object process() throws Exception {
        return result;
    }
}
