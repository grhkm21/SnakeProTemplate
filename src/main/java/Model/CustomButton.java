package Model;

import static Model.Preferences.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

// Reference: https://stackoverflow.com/questions/34750250/java-jbutton-with-changing-background
public class CustomButton extends JButton {
	public CustomButton(String text) {
		setText(text);
	}

	@Override
	public void paintComponent(Graphics g) {
		FontMetrics metrics = g.getFontMetrics(BUTTON_FONT);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setPaint(COLOR_BUTTON);
		g2.fillRoundRect(getWidth() / 20, getHeight() / 20, getWidth() * 9 / 10, getHeight() * 9 / 10, 30, 30);
		g2.setPaint(Color.BLACK);
		g2.setFont(BUTTON_FONT);
		int textX = (getWidth() - metrics.stringWidth(getText())) / 2;
		int textY = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
		g2.drawString(getText(), textX, textY);
		g2.dispose();
	}
}
