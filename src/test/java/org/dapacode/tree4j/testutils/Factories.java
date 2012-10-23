package org.dapacode.tree4j.testutils;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import org.dapacode.tree4j.common.Factory;

import java.util.Map;

public final class Factories {
  private Factories() { /* Utility class */ }

  public static final Factory<SetMultimap<String, String>> LINKED_HASH_MULTIMAP_FACTORY =
      new Factory<SetMultimap<String, String>>() {
        @Override
        public SetMultimap<String, String> get() {
          return LinkedHashMultimap.create();
        }
      };

  public static final Factory<Map<String, String>> HASH_MAP_FACTORY = new Factory<Map<String, String>>() {
    @Override
    public Map<String, String> get() {
      return Maps.newHashMap();
    }
  };

  public static final Factory<Map<String, String>> LINKED_HASH_MAP_FACTORY = new Factory<Map<String, String>>() {
    @Override
    public Map<String, String> get() {
      return Maps.newLinkedHashMap();
    }
  };
}
