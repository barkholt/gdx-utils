/** Copyright 2014 Robin Stumm (serverkorken@gmail.com, http://dermetfan.net)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package net.dermetfan.gdx.scenes.scene2d.ui.popup;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import net.dermetfan.gdx.scenes.scene2d.Scene2DUtils;

import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.enter;
import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.exit;
import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.touchDown;
import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.touchUp;

/** the Behavior of a classic tooltip
 *  @since 0.8.0
 *  @author dermetfan */
public class TooltipBehavior extends BasicBehavior {

	/** the mask bits */
	public static final byte mask = 1;

	private final PopupTask showTask = new PopupTask() {
		@Override
		public void run() {
			popup.show(event);
		}
	}, hideTask = new PopupTask() {
		@Override
		public void run() {
			popup.hide(event);
		}
	};

	/** the flags that define when to hide, show or cancel the tooltip */
	private int showFlags = mask << enter.ordinal(), hideFlags = mask << touchDown.ordinal() | mask << touchUp.ordinal() | mask << exit.ordinal(), cancelFlags = mask << touchDown.ordinal() | mask << exit.ordinal();

	/** the delay before {@link Popup#} */
	private float showDelay = .75f, hideDelay;

	@Override
	public Reaction handle(Event e, Popup popup) {
		if(!(e instanceof InputEvent))
			return super.handle(e, popup);
		InputEvent event = (InputEvent) e;

		if(event.getRelatedActor() == popup.getPopup())
			return super.handle(e, popup);

		int flag = mask << event.getType().ordinal();

		if((cancelFlags & flag) == flag)
			showTask.cancel();

		if((hideFlags & flag) == flag) {
			if(hideDelay > 0) {
				hideTask.init(event, popup);
				if(!hideTask.isScheduled())
					Timer.schedule(hideTask, hideDelay);
			} else
				return Reaction.Hide;
		}

		if((showFlags & flag) == flag) {
			if(showDelay > 0) {
				showTask.init(event, popup);
				if(!showTask.isScheduled())
					Timer.schedule(showTask, showDelay);
			} else
				return Reaction.Show;
		}
		return super.handle(e, popup);
	}

	/** @param flag the {@link Type} on which to show the tooltip */
	public void showOn(Type flag) {
		showFlags |= mask << flag.ordinal();
	}

	/** @param flag the {@link Type} on which not to show the tooltip */
	public void showNotOn(Type flag) {
		showFlags &= ~(mask << flag.ordinal());
	}

	/** @param flag the {@link Type} on which to hide the tooltip */
	public void hideOn(Type flag) {
		hideFlags |= mask << flag.ordinal();
	}

	/** @param flag the {@link Type} on which not to hide the tooltip */
	public void hideNotOn(Type flag) {
		hideFlags &= ~(mask << flag.ordinal());
	}

	/** @param flag the {@link Type} on which to cancel showing the tooltip */
	public void cancelOn(Type flag) {
		cancelFlags |= mask << flag.ordinal();
	}

	/** @param flag the {@link Type} on which to not cancel showing the tooltip */
	public void cancelNotOn(Type flag) {
		cancelFlags &= ~(mask << flag.ordinal());
	}

	/** never show the tooltip */
	public void showNever() {
		showFlags = 0;
	}

	/** never hide the tooltip */
	public void hideNever() {
		hideFlags = 0;
	}

	/** never cancel showing the tooltip */
	public void cancelNever() {
		cancelFlags = 0;
	}

	/** show the tooltip on any event */
	public void showAlways() {
		showFlags = ~0;
	}

	/** hide the tooltip on any event */
	public void hideAlways() {
		hideFlags = ~0;
	}

	/** cancel showing the tooltip on any event */
	public void cancelAlways() {
		cancelFlags = ~0;
	}

	/** @param delay the {@link #showDelay} and {@link #hideDelay} */
	public void setDelay(float delay) {
		showDelay = hideDelay = delay;
	}

	/** @return the {@link #showDelay} */
	public float getShowDelay() {
		return showDelay;
	}

	/** @param showDelay the {@link #showDelay} to set */
	public void setShowDelay(float showDelay) {
		this.showDelay = showDelay;
	}

	/** @return the {@link #hideDelay} */
	public float getHideDelay() {
		return hideDelay;
	}

	/** @param hideDelay the {@link #hideDelay} to set */
	public void setHideDelay(float hideDelay) {
		this.hideDelay = hideDelay;
	}

	/** @return the {@link #showFlags} */
	public int getShowFlags() {
		return showFlags;
	}

	/** @param showFlags the {@link #showFlags} to set */
	public void setShowFlags(int showFlags) {
		this.showFlags = showFlags;
	}

	/** @return the {@link #hideFlags} */
	public int getHideFlags() {
		return hideFlags;
	}

	/** @param hideFlags the {@link #hideFlags} to set */
	public void setHideFlags(int hideFlags) {
		this.hideFlags = hideFlags;
	}

	/** @return the {@link #cancelFlags} */
	public int getCancelFlags() {
		return cancelFlags;
	}

	/** @param cancelFlags the {@link #cancelFlags} to set */
	public void setCancelFlags(int cancelFlags) {
		this.cancelFlags = cancelFlags;
	}

	/** used internally to call {@link Popup#show(Event)} or {@link Popup#hide(Event)}
	 *  @since 0.8.0
	 *  @author dermetfan
	 *  @see #showTask
	 *  @see #hideTask */
	private static abstract class PopupTask extends Task {

		protected final Event event = new Event();
		protected Popup popup;

		public void init(Event event, Popup popup) {
			this.event.reset();
			Scene2DUtils.copy(event, this.event);
			this.popup = popup;
		}

	}

	/** provides {@link #followPointer}
	 *  @since 0.8.0
	 *  @author dermetfan */
	public static class TooltipPositionBehavior extends PositionBehavior {

		/** whether {@link Type#mouseMoved mouseMoved} events should apply the position */
		private boolean followPointer;

		/** @see PositionBehavior#PositionBehavior(Position) */
		public TooltipPositionBehavior(Position position) {
			super(position);
		}

		/** @param followPointer the {@link #followPointer} */
		public TooltipPositionBehavior(Position position, boolean followPointer) {
			super(position);
			this.followPointer = followPointer;
		}

		/** @return the {@link #followPointer} */
		public boolean isFollowPointer() {
			return followPointer;
		}

		/** @param followPointer the {@link #followPointer} to set */
		public void setFollowPointer(boolean followPointer) {
			this.followPointer = followPointer;
		}

		@Override
		public Reaction handle(Event event, Popup popup) {
			if(followPointer && event instanceof InputEvent && ((InputEvent) event).getType() == Type.mouseMoved)
				getPosition().apply(event, popup.getPopup());
			return super.handle(event, popup);
		}

	}

}
