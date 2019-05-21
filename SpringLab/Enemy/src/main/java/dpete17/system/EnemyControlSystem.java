package dpete17.system;

import dpete17.data.Entity;
import dpete17.data.GameData;
import dpete17.data.World;
import dpete17.data.entityparts.MovingPart;
import dpete17.data.entityparts.PositionPart;
import dpete17.services.IEntityProcessingService;
import org.springframework.stereotype.Service;

@Service
public class EnemyControlSystem implements IEntityProcessingService {
	private int repeatMove = 0;
	private float chanceMove = 0;

	@Override
	public void process(GameData gameData, World world) {

		for (Entity enemy : world.getEntities(Enemy.class)) {
			PositionPart positionPart = enemy.getPart(PositionPart.class);
			MovingPart movingPart = enemy.getPart(MovingPart.class);

			// CHANCE
			if(repeatMove == 0) {
				resetDirection(movingPart);

				chanceMove = (float) (1 + Math.random() * 100);
				repeatMove = (int) (10 + Math.random() * 10);
			}

			if(chanceMove > 80) {
				movingPart.setLeft(true);
			} else if(chanceMove > 60) {
				movingPart.setRight(true);
			} else {
				movingPart.setUp(true);
			}

			repeatMove--;

			movingPart.process(gameData, enemy);

			updateShape(enemy);
		}
	}

	private void resetDirection(MovingPart part) {
		part.setUp(false);
		part.setLeft(false);
		part.setRight(false);
	}

	private void updateShape(Entity entity) {
		float[] shapex = entity.getShapeX();
		float[] shapey = entity.getShapeY();
		PositionPart positionPart = entity.getPart(PositionPart.class);
		float x = positionPart.getX();
		float y = positionPart.getY();
		float radians = positionPart.getRadians();

		shapex[0] = (float) (x + Math.cos(radians) * 8);
		shapey[0] = (float) (y + Math.sin(radians) * 8);

		shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5) * 8);
		shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5) * 8);

		shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * 5);
		shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * 5);

		shapex[3] = (float) (x + Math.cos(radians + 4 * 3.1415f / 5) * 8);
		shapey[3] = (float) (y + Math.sin(radians + 4 * 3.1415f / 5) * 8);

		entity.setShapeX(shapex);
		entity.setShapeY(shapey);
	}

}