package org.datalift.sdmxdatacube;


import java.text.MessageFormat;


/**
 * <code>TechnicalExceptions</code> are thrown to indicate technical
 * or environmental (i.e. configuration, hardware...) error conditions
 * that need not to be handled by the application code. These errors
 * shall be notified to the system administrator.
 *
 * @author  lbihanic
 */
public class TechnicalException extends org.datalift.fwk.TechnicalException
{
    //-------------------------------------------------------------------------
    // Constructors
    //-------------------------------------------------------------------------

    /**
     * Constructs a new exception with the specified cause but no
     * detail message.  The detail message of this exception will
     * be the detail message of the cause.
     * @param  cause   the cause.
     */
    public TechnicalException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified message code
     * and arguments to build a detail message from the format
     * associated to the message code.
     * <p>
     * The message code can be either the actual message format or
     * the identifier of a resource (defined in the
     * {@link #getMessageBundleName exception type resource bundle})
     * that contains the message format.</p>
     * @param  code   the message code or format. In the latter
     *                case, it shall be compliant with the
     *                grammar defined by {@link MessageFormat}.
     * @param  data   the arguments to build the detail message
     *                from the format.
     */
    public TechnicalException(String code, Object... data) {
        super(code, data);
    }

    /**
     * Constructs a new exception with the specified message code
     * and the arguments to build a detail message from the format
     * associated to the message code.
     * <p>
     * The message code can be either the actual message format or
     * the identifier of a resource (defined in the
     * {@link #getMessageBundleName exception type resource bundle})
     * that contains the message format.</p>
     * <p>
     * Note that the detail message associated with
     * <code>cause</code> is <em>not</em> automatically incorporated
     * in this exception's detail message.</p>
     * @param  code    the message code or format. In the latter
     *                 case, it shall be compliant with the
     *                 grammar defined by {@link MessageFormat}.
     * @param  cause   the cause. A <code>null</code> value is
     *                 allowed to indicate that the cause is
     *                 nonexistent or unknown.
     * @param  data    the arguments to build the detail message
     *                 from the format.
     */
    public TechnicalException(String code, Throwable cause, Object... data) {
        super(code, cause, data);
    }
}
