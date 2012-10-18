package org.dapacode.tree4j.testutils;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.dapacode.tree4j.common.Factory;
import org.dapacode.tree4j.common.MutableMap;
import org.dapacode.tree4j.common.MutableMaps;
import org.dapacode.tree4j.common.MutableMultimap;
import org.dapacode.tree4j.common.MutableMultimaps;

import java.util.Map;

public final class Factories {
  private Factories() { /* Utility class */ }

  public static final Factory<MutableMultimap<String, String>> LINKED_HASH_MULTIMAP_FACTORY =
      new Factory<MutableMultimap<String, String>>() {
        @Override
        public MutableMultimap<String, String> get() {
          final Multimap<String, String> multimap = LinkedHashMultimap
              .create();
          return MutableMultimaps.wrap(multimap);
        }
      };

  public static final Factory<MutableMap<String, String>> HASH_MAP_FACTORY = new Factory<MutableMap<String, String>>() {
    @Override
    public MutableMap<String, String> get() {
      final Map<String, String> map = Maps.newHashMap();
      return MutableMaps.wrap(map);
    }
  };

  public static final Factory<MutableMap<String, String>> LINKED_HASH_MAP_FACTORY = new Factory<MutableMap<String, String>>() {
    @Override
    public MutableMap<String, String> get() {
      final Map<String, String> map = Maps.newLinkedHashMap();
      return MutableMaps.wrap(map);
    }
  };
}
