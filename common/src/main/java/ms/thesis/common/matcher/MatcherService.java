package ms.thesis.common.matcher;

import ms.thesis.common.record.ByteArrayRecord;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

public interface MatcherService {

  /**
   * Adds a new matching rule
   *
   * @param id           an ID for the matching rule
   * @param matchingRule the matching rule
   */
  void addMatchingRule(String id, MatchingRule matchingRule);

  /**
   * Removes a matching rule
   *
   * @param id
   * @return
   */
  boolean removeMatchingRule(String id);

  /**
   * Processes a record, and calls the corresponding record consumers.
   *
   * @param record
   */
  Stream<Map.Entry<String, ByteArrayRecord>> match(ByteArrayRecord record);
}
