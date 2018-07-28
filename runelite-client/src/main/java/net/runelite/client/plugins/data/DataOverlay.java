package net.runelite.client.plugins.data;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Map;

@Slf4j
public class DataOverlay extends Overlay
{
	private final Client client;
	private final DataPlugin plugin;

	@Inject
	private DataOverlay(Client client, DataPlugin plugin)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		this.client = client;
		this.plugin = plugin;
	}


	@Override
	public Dimension render(Graphics2D graphics)
	{
		plugin.getEmptyRocks().forEach((object, tile) ->
		{
			Polygon polygon = object.getCanvasTilePoly();
			if (polygon != null)
			{
				OverlayUtil.renderPolygon(graphics, polygon, Color.GRAY);
			}

		});

		plugin.getFullRocks().forEach((object, tile) ->
		{
			Polygon polygon = object.getCanvasTilePoly();
			if (polygon != null)
			{
				OverlayUtil.renderPolygon(graphics, polygon, Color.CYAN);
			}

		});

		TileObject closest = null;
		double distance = 999999.0D;

		for (Map.Entry<TileObject, Tile> entry : plugin.getFullRocks().entrySet())
		{
			double d = client.getLocalPlayer().getLocalLocation().distanceTo(entry.getKey().getLocalLocation());
			if (d <= distance)
			{
				distance = d;
				closest = entry.getKey();
			}
		}

		if (closest != null)
		{
			Polygon polygon = closest.getCanvasTilePoly();
			if (polygon != null)
			{
				OverlayUtil.renderPolygon(graphics, polygon, Color.GREEN);
			}
		}


		return null;
	}
}
