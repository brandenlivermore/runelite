package net.runelite.client.plugins.data;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import static net.runelite.api.ObjectID.*;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameObjectChanged;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@PluginDescriptor(
	name = "Data ;)",
	description = "does data things"
)
@Slf4j
public class DataPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private DataOverlay dataOverlay;

	@Getter
	private final Map<TileObject, Tile> fullRocks = new HashMap<>();

	@Getter
	private final Map<TileObject, Tile> emptyRocks = new HashMap<>();

	private Set<Integer> emptyRockSet = ImmutableSet.of(
		ROCKS_7468, ROCKS_7469 // empty
	);

	private Set<Integer> fullRockSet = ImmutableSet.of(
		ROCKS_7453, ROCKS_7484, // copper
		ROCKS_7485, ROCKS_7486 // tin
	);

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(dataOverlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(dataOverlay);
		fullRocks.clear();
		emptyRocks.clear();
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		log.info("Rocks: " + fullRocks.size());
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		onTileObject(event.getTile(), null, event.getGameObject());
	}

	@Subscribe
	public void onGameObjectChanged(GameObjectChanged event)
	{
		onTileObject(event.getTile(), event.getPrevious(), event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDeSpawned(GameObjectDespawned event)
	{
		onTileObject(event.getTile(), event.getGameObject(), null);
	}

	private void onTileObject(Tile tile, TileObject oldObject, TileObject newObject)
	{
		fullRocks.remove(oldObject);
		emptyRocks.remove(oldObject);

		if (newObject == null)
		{
			return;
		}

		if (fullRockSet.contains(newObject.getId()))
		{
			fullRocks.put(newObject, tile);
		}

		if (emptyRockSet.contains(newObject.getId()))
		{
			emptyRocks.put(newObject, tile);
		}
	}
}
