import org.lwjgl.LWJGLException
import org.lwjgl.opengl.AWTGLCanvas
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11
//import org.lwjgl.util.glu.GLU
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

public class SimpleCanvas extends AWTGLCanvas {
	def script
	ScriptEngine engine
	float angle = 0
	public SimpleCanvas() throws LWJGLException {
		// Launch a thread to repaint the canvas 60 fps
		Thread.start {
			while(true) {
				if (isVisible()) {
					repaint()
				}
				Display.sync(60)
			}
		}
	}
	
	public void paintGL() {
		engine?.put("GL11", GL11)
		engine?.put("angle", angle)
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
		if (script != null)
			engine.eval(script)
		
		/*GL11.with {
			glClear(GL11.GL_COLOR_BUFFER_BIT)
			glMatrixMode(GL11.GL_PROJECTION_MATRIX)
			glLoadIdentity()
			glOrtho(0, 640, 0, 480, 1, -1)
			glMatrixMode(GL11.GL_MODELVIEW_MATRIX)

			glPushMatrix()
			glTranslatef(320, 240, 0.0f)
			glRotatef(angle, 0, 0, 1.0f)
			glBegin(GL11.GL_QUADS)
			glVertex2i(-50, -50)
			glVertex2i(50, -50)
			glVertex2i(50, 50)
			glVertex2i(-50, 50)
			glEnd()
			glPopMatrix()
		}*/
		
		angle += 1
		try {
			swapBuffers()
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
}