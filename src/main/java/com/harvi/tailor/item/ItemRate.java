package com.harvi.tailor.item;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/** A persisted rate override for an item. The {@code id} is the item's id. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "itemRate")
public class ItemRate {

  @Id private String id;
  private int rate;
}
