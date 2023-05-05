package ms.thesis.common.matcher;

import ms.thesis.common.record.ByteArrayRecord;

import java.util.function.Predicate;

/**
 * A stream query that is a predicate.
 */
public interface MatchingRule extends Predicate<ByteArrayRecord> {
}
