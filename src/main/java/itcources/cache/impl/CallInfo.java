package itcources.cache.impl;

/**
* @author Nikita Konovalov
*/
public class CallInfo {
    private long callTimestamp;
    private Object result;

    public CallInfo(long callTimestamp, Object result) {
        this.callTimestamp = callTimestamp;
        this.result = result;
    }

    public long getCallTimestamp() {
        return callTimestamp;
    }

    public void setCallTimestamp(long callTimestamp) {
        this.callTimestamp = callTimestamp;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
