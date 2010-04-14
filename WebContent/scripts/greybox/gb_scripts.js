var GB_CURRENT = null;
GB_hide = function(a) {
	GB_CURRENT.hide(a)
};
GreyBox = new AJS.Class( {
	init : function(c) {
		this.use_fx = AJS.fx;
		this.type = "page";
		this.overlay_click_close = false;
		this.salt = 0;
		this.root_dir = GB_ROOT_DIR;
		this.callback_fns = [];
		this.reload_on_close = false;
		this.src_loader = this.root_dir + "loader_frame.htm";
		var b = window.location.hostname.indexOf("www");
		var a = this.src_loader.indexOf("www");
		if (b != -1 && a == -1) {
			this.src_loader = this.src_loader.replace("://", "://www.")
		}
		if (b == -1 && a != -1) {
			this.src_loader = this.src_loader.replace("://www.", "://")
		}
		this.show_loading = true;
		AJS.update(this, c)
	},
	addCallback : function(a) {
		if (a) {
			this.callback_fns.push(a)
		}
	},
	show : function(a) {
		GB_CURRENT = this;
		this.url = a;
		var b = [ AJS.$bytc("object"), AJS.$bytc("select") ];
		AJS.map(AJS.flattenList(b), function(c) {
			c.style.visibility = "hidden"
		});
		this.createElements();
		return false
	},
	hide : function(a) {
		var b = this;
		setTimeout( function() {
			var d = b.callback_fns;
			if (d != []) {
				AJS.map(d, function(f) {
					f()
				})
			}
			b.onHide();
			if (b.use_fx) {
				var e = b.overlay;
				AJS.fx.fadeOut(b.overlay, {
					onComplete : function() {
						AJS.removeElement(e);
						e = null
					},
					duration :300
				});
				AJS.removeElement(b.g_window)
			} else {
				AJS.removeElement(b.g_window, b.overlay)
			}
			b.removeFrame();
			AJS.REV(window, "scroll", _GB_setOverlayDimension);
			AJS.REV(window, "resize", _GB_update);
			var c = [ AJS.$bytc("object"), AJS.$bytc("select") ];
			AJS.map(AJS.flattenList(c), function(f) {
				f.style.visibility = "visible"
			});
			GB_CURRENT = null;
			if (b.reload_on_close) {
				window.location.reload()
			}
			if (AJS.isFunction(a)) {
				a()
			}
		}, 10)
	},
	update : function() {
		this.setOverlayDimension();
		this.setFrameSize();
		this.setWindowPosition()
	},
	createElements : function() {
		this.initOverlay();
		this.g_window = AJS.DIV( {
			id :"GB_window"
		});
		AJS.hideElement(this.g_window);
		AJS.getBody().insertBefore(this.g_window, this.overlay.nextSibling);
		this.initFrame();
		this.initHook();
		this.update();
		var a = this;
		if (this.use_fx) {
			AJS.fx.fadeIn(this.overlay, {
				duration :300,
				to :0.7,
				onComplete : function() {
					a.onShow();
					AJS.showElement(a.g_window);
					a.startLoading()
				}
			})
		} else {
			AJS.setOpacity(this.overlay, 0.7);
			AJS.showElement(this.g_window);
			this.onShow();
			this.startLoading()
		}
		AJS.AEV(window, "scroll", _GB_setOverlayDimension);
		AJS.AEV(window, "resize", _GB_update)
	},
	removeFrame : function() {
		try {
			AJS.removeElement(this.iframe)
		} catch (a) {
		}
		this.iframe = null
	},
	startLoading : function() {
		this.iframe.src = this.src_loader + "?s=" + this.salt++;
		AJS.showElement(this.iframe)
	},
	setOverlayDimension : function() {
		var b = AJS.getWindowSize();
		if (AJS.isMozilla() || AJS.isOpera()) {
			AJS.setWidth(this.overlay, "100%")
		} else {
			AJS.setWidth(this.overlay, b.w)
		}
		var a = Math.max(AJS.getScrollTop() + b.h, AJS.getScrollTop()
				+ this.height);
		if (a < AJS.getScrollTop()) {
			AJS.setHeight(this.overlay, a)
		} else {
			AJS.setHeight(this.overlay, AJS.getScrollTop() + b.h)
		}
	},
	initOverlay : function() {
		this.overlay = AJS.DIV( {
			id :"GB_overlay"
		});
		if (this.overlay_click_close) {
			AJS.AEV(this.overlay, "click", GB_hide)
		}
		AJS.setOpacity(this.overlay, 0);
		AJS.getBody().insertBefore(this.overlay, AJS.getBody().firstChild)
	},
	initFrame : function() {
		if (!this.iframe) {
			var a = {
				name :"GB_frame",
				"class" :"GB_frame",
				frameBorder :0
			};
			if (AJS.isIe()) {
				a.src = 'javascript:false;document.write("");'
			}
			this.iframe = AJS.IFRAME(a);
			this.middle_cnt = AJS.DIV( {
				"class" :"content"
			}, this.iframe);
			this.top_cnt = AJS.DIV();
			this.bottom_cnt = AJS.DIV();
			AJS.ACN(this.g_window, this.top_cnt, this.middle_cnt,
					this.bottom_cnt)
		}
	},
	onHide : function() {
	},
	onShow : function() {
	},
	setFrameSize : function() {
	},
	setWindowPosition : function() {
	},
	initHook : function() {
	}
});
_GB_update = function() {
	if (GB_CURRENT) {
		GB_CURRENT.update()
	}
};
_GB_setOverlayDimension = function() {
	if (GB_CURRENT) {
		GB_CURRENT.setOverlayDimension()
	}
};
AJS.preloadImages(GB_ROOT_DIR + "indicator.gif");
script_loaded = true;
var GB_SETS = {};
function decoGreyboxLinks() {
	var a = AJS.$bytc("a");
	AJS.map(a, function(c) {
		if (c.getAttribute("href") && c.getAttribute("rel")) {
			var b = c.getAttribute("rel");
			if (b.indexOf("gb_") == 0) {
				var f = b.match(/\w+/)[0];
				var e = b.match(/\[(.*)\]/)[1];
				var d = 0;
				var g = {
					caption :c.title || "",
					url :c.href
				};
				if (f == "gb_pageset" || f == "gb_imageset") {
					if (!GB_SETS[e]) {
						GB_SETS[e] = []
					}
					GB_SETS[e].push(g);
					d = GB_SETS[e].length
				}
				if (f == "gb_pageset") {
					c.onclick = function() {
						GB_showFullScreenSet(GB_SETS[e], d);
						return false
					}
				}
				if (f == "gb_imageset") {
					c.onclick = function() {
						GB_showImageSet(GB_SETS[e], d);
						return false
					}
				}
				if (f == "gb_image") {
					c.onclick = function() {
						GB_showImage(g.caption, g.url);
						return false
					}
				}
				if (f == "gb_page") {
					c.onclick = function() {
						var h = e.split(/, ?/);
						GB_show(g.caption, g.url, parseInt(h[1]),
								parseInt(h[0]));
						return false
					}
				}
				if (f == "gb_page_fs") {
					c.onclick = function() {
						GB_showFullScreen(g.caption, g.url);
						return false
					}
				}
				if (f == "gb_page_center") {
					c.onclick = function() {
						var h = e.split(/, ?/);
						GB_showCenter(g.caption, g.url, parseInt(h[1]),
								parseInt(h[0]));
						return false
					}
				}
			}
		}
	})
}
AJS.AEV(window, "load", decoGreyboxLinks);
GB_showImage = function(a, c, e) {
	var b = {
		width :300,
		height :300,
		type :"image",
		fullscreen :false,
		center_win :true,
		caption :a,
		callback_fn :e
	};
	var d = new GB_Gallery(b);
	return d.show(c)
};
GB_showPage = function(a, c, e) {
	var b = {
		type :"page",
		caption :a,
		callback_fn :e,
		fullscreen :true,
		center_win :false
	};
	var d = new GB_Gallery(b);
	return d.show(c)
};
GB_Gallery = GreyBox.extend( {
	init : function(a) {
		this.parent( {});
		this.img_close = this.root_dir + "g_close.gif";
		AJS.update(this, a);
		this.addCallback(this.callback_fn)
	},
	initHook : function() {
		AJS.addClass(this.g_window, "GB_Gallery");
		var c = AJS.DIV( {
			"class" :"inner"
		});
		this.header = AJS.DIV( {
			"class" :"GB_header"
		}, c);
		AJS.setOpacity(this.header, 0);
		AJS.getBody().insertBefore(this.header, this.overlay.nextSibling);
		var e = AJS.TD( {
			id :"GB_caption",
			"class" :"caption",
			width :"40%"
		}, this.caption);
		var b = AJS.TD( {
			id :"GB_middle",
			"class" :"middle",
			width :"20%"
		});
		var f = AJS.IMG( {
			src :this.img_close
		});
		AJS.AEV(f, "click", GB_hide);
		var a = AJS.TD( {
			"class" :"close",
			width :"40%"
		}, f);
		var d = AJS.TBODY(AJS.TR(e, b, a));
		var g = AJS.TABLE( {
			cellspacing :"0",
			cellpadding :0,
			border :0
		}, d);
		AJS.ACN(c, g);
		if (this.fullscreen) {
			AJS.AEV(window, "scroll", AJS.$b(this.setWindowPosition, this))
		} else {
			AJS.AEV(window, "scroll", AJS.$b(this._setHeaderPos, this))
		}
	},
	setFrameSize : function() {
		var b = this.overlay.offsetWidth;
		var a = AJS.getWindowSize();
		if (this.fullscreen) {
			this.width = b - 40;
			this.height = a.h - 80
		}
		AJS.setWidth(this.iframe, this.width);
		AJS.setHeight(this.iframe, this.height);
		AJS.setWidth(this.header, b)
	},
	_setHeaderPos : function() {
		AJS.setTop(this.header, AJS.getScrollTop() + 10)
	},
	setWindowPosition : function() {
		var c = this.overlay.offsetWidth;
		var a = AJS.getWindowSize();
		AJS.setLeft(this.g_window, ((c - 50 - this.width) / 2));
		var d = AJS.getScrollTop() + 55;
		if (!this.center_win) {
			AJS.setTop(this.g_window, d)
		} else {
			var b = ((a.h - this.height) / 2) + 20 + AJS.getScrollTop();
			if (b < 0) {
				b = 0
			}
			if (d > b) {
				b = d
			}
			AJS.setTop(this.g_window, b)
		}
		this._setHeaderPos()
	},
	onHide : function() {
		AJS.removeElement(this.header);
		AJS.removeClass(this.g_window, "GB_Gallery")
	},
	onShow : function() {
		if (this.use_fx) {
			AJS.fx.fadeIn(this.header, {
				to :1
			})
		} else {
			AJS.setOpacity(this.header, 1)
		}
	}
});
AJS.preloadImages(GB_ROOT_DIR + "g_close.gif");
GB_showFullScreenSet = function(e, a, d) {
	var b = {
		type :"page",
		fullscreen :true,
		center_win :false
	};
	var c = new GB_Sets(b, e);
	c.addCallback(d);
	c.showSet(a - 1);
	return false
};
GB_showImageSet = function(e, a, d) {
	var b = {
		type :"image",
		fullscreen :false,
		center_win :true,
		width :300,
		height :300
	};
	var c = new GB_Sets(b, e);
	c.addCallback(d);
	c.showSet(a - 1);
	return false
};
GB_Sets = GB_Gallery.extend( {
	init : function(a, b) {
		this.parent(a);
		if (!this.img_next) {
			this.img_next = this.root_dir + "next.gif"
		}
		if (!this.img_prev) {
			this.img_prev = this.root_dir + "prev.gif"
		}
		this.current_set = b
	},
	showSet : function(a) {
		this.current_index = a;
		var b = this.current_set[this.current_index];
		this.show(b.url);
		this._setCaption(b.caption);
		this.btn_prev = AJS.IMG( {
			"class" :"left",
			src :this.img_prev
		});
		this.btn_next = AJS.IMG( {
			"class" :"right",
			src :this.img_next
		});
		AJS.AEV(this.btn_prev, "click", AJS.$b(this.switchPrev, this));
		AJS.AEV(this.btn_next, "click", AJS.$b(this.switchNext, this));
		GB_STATUS = AJS.SPAN( {
			"class" :"GB_navStatus"
		});
		AJS.ACN(AJS.$("GB_middle"), this.btn_prev, GB_STATUS, this.btn_next);
		this.updateStatus()
	},
	updateStatus : function() {
		AJS.setHTML(GB_STATUS, (this.current_index + 1) + " / "
				+ this.current_set.length);
		if (this.current_index == 0) {
			AJS.addClass(this.btn_prev, "disabled")
		} else {
			AJS.removeClass(this.btn_prev, "disabled")
		}
		if (this.current_index == this.current_set.length - 1) {
			AJS.addClass(this.btn_next, "disabled")
		} else {
			AJS.removeClass(this.btn_next, "disabled")
		}
	},
	_setCaption : function(a) {
		AJS.setHTML(AJS.$("GB_caption"), a)
	},
	updateFrame : function() {
		var a = this.current_set[this.current_index];
		this._setCaption(a.caption);
		this.url = a.url;
		this.startLoading()
	},
	switchPrev : function() {
		if (this.current_index != 0) {
			this.current_index--;
			this.updateFrame();
			this.updateStatus()
		}
	},
	switchNext : function() {
		if (this.current_index != this.current_set.length - 1) {
			this.current_index++;
			this.updateFrame();
			this.updateStatus()
		}
	}
});
AJS.AEV(window, "load", function() {
	AJS.preloadImages(GB_ROOT_DIR + "next.gif", GB_ROOT_DIR + "prev.gif")
});
GB_show = function(b, d, a, e, g) {
	var c = {
		caption :b,
		height :a || 500,
		width :e || 500,
		fullscreen :false,
		callback_fn :g
	};
	var f = new GB_Window(c);
	return f.show(d)
};
GB_showCenter = function(b, d, a, e, g) {
	var c = {
		caption :b,
		center_win :true,
		height :a || 500,
		width :e || 500,
		fullscreen :false,
		callback_fn :g
	};
	var f = new GB_Window(c);
	return f.show(d)
};
GB_showFullScreen = function(a, c, e) {
	var b = {
		caption :a,
		fullscreen :true,
		callback_fn :e
	};
	var d = new GB_Window(b);
	return d.show(c)
};
GB_Window = GreyBox.extend( {
	init : function(a) {
		this.parent( {});
		this.img_header = this.root_dir + "header_bg.gif";
		this.img_close = this.root_dir + "w_close.gif";
		this.show_close_img = true;
		AJS.update(this, a);
		this.addCallback(this.callback_fn)
	},
	initHook : function() {
		AJS.addClass(this.g_window, "GB_Window");
		this.header = AJS.TABLE( {
			"class" :"header"
		});
		this.header.style.backgroundImage = "url(" + this.img_header + ")";
		var b = AJS.TD( {
			"class" :"caption"
		}, this.caption);
		var a = AJS.TD( {
			"class" :"close"
		});
		if (this.show_close_img) {
			var e = AJS.IMG( {
				src :this.img_close
			});
			var d = AJS.SPAN("Close");
			var c = AJS.DIV(e, d);
			AJS.AEV( [ e, d ], "mouseover", function() {
				AJS.addClass(d, "on")
			});
			AJS.AEV( [ e, d ], "mouseout", function() {
				AJS.removeClass(d, "on")
			});
			AJS.AEV( [ e, d ], "mousedown", function() {
				AJS.addClass(d, "click")
			});
			AJS.AEV( [ e, d ], "mouseup", function() {
				AJS.removeClass(d, "click")
			});
			AJS.AEV( [ e, d ], "click", GB_hide);
			AJS.ACN(a, c)
		}
		tbody_header = AJS.TBODY();
		AJS.ACN(tbody_header, AJS.TR(b, a));
		AJS.ACN(this.header, tbody_header);
		AJS.ACN(this.top_cnt, this.header);
		if (this.fullscreen) {
			AJS.AEV(window, "scroll", AJS.$b(this.setWindowPosition, this))
		}
	},
	setFrameSize : function() {
		if (this.fullscreen) {
			var a = AJS.getWindowSize();
			overlay_h = a.h;
			this.width = Math.round(this.overlay.offsetWidth
					- (this.overlay.offsetWidth / 100) * 10);
			this.height = Math.round(overlay_h - (overlay_h / 100) * 10)
		}
		AJS.setWidth(this.header, this.width + 6);
		AJS.setWidth(this.iframe, this.width);
		AJS.setHeight(this.iframe, this.height)
	},
	setWindowPosition : function() {
		var a = AJS.getWindowSize();
		AJS.setLeft(this.g_window, ((a.w - this.width) / 2) - 13);
		if (!this.center_win) {
			AJS.setTop(this.g_window, AJS.getScrollTop())
		} else {
			var b = ((a.h - this.height) / 2) - 20 + AJS.getScrollTop();
			if (b < 0) {
				b = 0
			}
			AJS.setTop(this.g_window, b)
		}
	}
});
AJS.preloadImages(GB_ROOT_DIR + "w_close.gif", GB_ROOT_DIR + "header_bg.gif");
script_loaded = true;