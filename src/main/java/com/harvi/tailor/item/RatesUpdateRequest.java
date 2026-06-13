package com.harvi.tailor.item;

import java.util.Map;

/** Map of item id to new rate. */
public record RatesUpdateRequest(Map<String, Integer> rates) {}
