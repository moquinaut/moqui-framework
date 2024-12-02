/*
 * This software is in the public domain under CC0 1.0 Universal plus a 
 * Grant of Patent License.
 * 
 * To the extent possible under law, the author(s) have dedicated all
 * copyright and related and neighboring rights to this software to the
 * public domain worldwide. This software is distributed without any
 * warranty.
 * 
 * You should have received a copy of the CC0 Public Domain Dedication
 * along with this software (see the LICENSE.md file). If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.moqui.impl.context

import org.moqui.context.LoggerFacade
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class LoggerFacadeImpl implements LoggerFacade {
    protected final static Logger logger = LoggerFactory.getLogger(LoggerFacadeImpl.class)

    protected final ExecutionContextFactoryImpl ecfi

    protected boolean traceEnabled, debugEnabled, infoEnabled, warnEnabled, errorEnabled

    LoggerFacadeImpl(ExecutionContextFactoryImpl ecfi) {
        this.ecfi = ecfi
        this.traceEnabled = false
        this.debugEnabled = false
        this.infoEnabled = true
        this.warnEnabled = true
        this.errorEnabled = true
    }

    void log(String levelStr, String message, Throwable thrown) {
        int level
        switch (levelStr) {
            case "trace": level = TRACE_INT; break
            case "debug": level = DEBUG_INT; break
            case "info": level = INFO_INT; break
            case "warn": level = WARN_INT; break
            case "error": level = ERROR_INT; break
            case "off": // do nothing
            default: return
        }
        log(level, message, thrown)
    }

    @Override
    void log(int level, String message, Throwable thrown) {
        if (level == TRACE_INT && traceEnabled) {
            logger.trace(message, thrown)
        } else if (level == DEBUG_INT && debugEnabled) {
            logger.debug(message, thrown)
        } else if (level == INFO_INT && infoEnabled) {
            logger.info(message, thrown)
        } else if (level == WARN_INT && warnEnabled) {
            logger.warn(message, thrown)
        } else if (level == ERROR_INT && errorEnabled) {
            logger.error(message, thrown)
        } else if (level == FATAL_INT && errorEnabled) {
            logger.error(message, thrown)
        } else if (level == ALL_INT) {
            logger.warn(message, thrown)
        }
    }

    void trace(String message) { log(TRACE_INT, message, null) }

    void debug(String message) { log(DEBUG_INT, message, null) }

    void info(String message) { log(INFO_INT, message, null) }

    void warn(String message) { log(WARN_INT, message, null) }

    void error(String message) { log(ERROR_INT, message, null) }

    void trace(String message, Throwable thrown) { log(TRACE_INT, message, thrown) }

    void debug(String message, Throwable thrown) { log(DEBUG_INT, message, thrown) }

    void info(String message, Throwable thrown) { log(INFO_INT, message, thrown) }

    void warn(String message, Throwable thrown) { log(WARN_INT, message, thrown) }

    void error(String message, Throwable thrown) { log(ERROR_INT, message, thrown) }

    boolean isLevelEnabled(int level) {
        switch (level) {
            case TRACE_INT: return traceEnabled
            case DEBUG_INT: return debugEnabled
            case INFO_INT: return infoEnabled
            case WARN_INT: return warnEnabled
            case ERROR_INT: case FATAL_INT: return errorEnabled
            case ALL_INT: return traceEnabled && debugEnabled && infoEnabled && warnEnabled && errorEnabled
            case OFF_INT: return !traceEnabled && !debugEnabled && !infoEnabled && !warnEnabled && !errorEnabled
            default: return false
        }
    }

    void setLevelEnabled(int level, boolean enabled) {
        switch (level) {
            case TRACE_INT: traceEnabled = enabled; break
            case DEBUG_INT: debugEnabled = enabled; break
            case INFO_INT: infoEnabled = enabled; break
            case WARN_INT: warnEnabled = enabled; break
            case ERROR_INT: case FATAL_INT: errorEnabled = enabled; break
            case ALL_INT:
                traceEnabled = enabled
                debugEnabled = enabled
                infoEnabled = enabled
                warnEnabled = enabled
                errorEnabled = enabled
                break
            default: break
        }
    }
}
