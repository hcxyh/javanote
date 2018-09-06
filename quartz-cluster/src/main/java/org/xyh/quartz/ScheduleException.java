package org.xyh.quartz;

import com.dexcoder.commons.exceptions.DexcoderException;

/**
 * description : 定时任务自定义异常
 */
public class ScheduleException extends DexcoderException {

    /** serialVersionUID */
    private static final long serialVersionUID = -1921648378954132894L;

    /**
     * Instantiates a new ScheduleException.
     */
    public ScheduleException(Throwable e) {
        super(e);
    }

    /**
     * Constructor
     */
    public ScheduleException(String message) {
        super(message);
    }

    /**
     * Constructor
     */
    public ScheduleException(String code, String message) {
        super(code, message);
    }
}
