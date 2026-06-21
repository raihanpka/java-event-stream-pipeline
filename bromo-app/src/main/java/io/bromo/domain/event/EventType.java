package io.bromo.domain.event;

import java.util.Set;

/**
 * Event type registry. Bromo is schema-agnostic - any type passes through. This registry only
 * contains the example types shipped in the boilerplate; replace with your own domain types.
 *
 * <p>Topic naming: {@code <source>.<type>.v<version>} (see AGENTS.md §5).
 */
public final class EventType {

  public static final String COMPLETION_V1 = "com.example.completion.v1";
  public static final String ACTION_V1 = "com.example.action.v1";
  public static final String FEEDBACK_V1 = "com.example.feedback.v1";

  private static final Set<String> KNOWN = Set.of(COMPLETION_V1, ACTION_V1, FEEDBACK_V1);

  private EventType() {}

  public static boolean isKnown(String type) {
    return KNOWN.contains(type);
  }
}
