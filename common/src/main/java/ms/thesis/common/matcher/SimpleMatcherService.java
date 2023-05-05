package ms.thesis.common.matcher;

import ms.thesis.common.record.ByteArrayRecord;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SimpleMatcherService implements MatcherService {

  private final Map<String, MatchingRule> matchingRuleEntries = new HashMap<>();

  @Override
  public void addMatchingRule(String id, MatchingRule matchingRule) {
    matchingRuleEntries.put(id, matchingRule);
  }

  @Override
  public boolean removeMatchingRule(String id) {
    return matchingRuleEntries.remove(id) != null;
  }

  @Override
  public Stream<Map.Entry<String, ByteArrayRecord>> match(ByteArrayRecord record) {
    return matchingRuleEntries.entrySet()
        .stream()
        .filter(entry -> entry.getValue().test(record))
        .map(rule -> Map.entry(rule.getKey(), record));
  }
}
