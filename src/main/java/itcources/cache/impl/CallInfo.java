package itcources.cache.impl;

/**
* @author Nikita Konovalov
*/
public class CallInfo {
    private long callTimestamp;
    private Object result;
    private Exception e;

    public CallInfo(long callTimestamp, Object result, Exception e) {
        this.callTimestamp = callTimestamp;
        this.result = result;
        this.e = e;
    }

    public long getCallTimestamp() {
        return callTimestamp;
    }

    public Object getResult() {
        return result;
    }

    public Exception getException() {
        return e;
    }
}
