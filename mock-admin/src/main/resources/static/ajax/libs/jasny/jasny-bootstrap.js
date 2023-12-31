/*!
 * Jasny Bootstrap v4.0.0 (http://jasny.github.io/bootstrap)
 * Copyright 2012-2014 Arnold Daniels
 * Licensed under Apache-2.0 (https://github.com/jasny/bootstrap/blob/master/LICENSE)
 */

+function ($) {
    "use strict";

    var isIE = window.navigator.appName == 'Microsoft Internet Explorer'

    // FILEUPLOAD PUBLIC CLASS DEFINITION
    // =================================

    var Fileinput = function (element, options) {
        this.$element = $(element)

        this.options = $.extend({}, Fileinput.DEFAULTS, options)
        this.$input = this.$element.find(':file')
        if (this.$input.length === 0) return

        this.name = this.$input.attr('name') || options.name

        this.$hidden = this.$element.find('input[type=hidden][name="' + this.name + '"]')
        if (this.$hidden.length === 0) {
            this.$hidden = $('<input type="hidden">').insertBefore(this.$input)
        }

        this.$preview = this.$element.find('.fileinput-preview')
        var height = this.$preview.css('height')
        if (this.$preview.css('display') !== 'inline' && height !== '0px' && height !== 'none') {
            this.$preview.css('line-height', height)
        }

        this.original = {
            exists: this.$element.hasClass('fileinput-exists'),
            preview: this.$preview.html(),
            hiddenVal: this.$hidden.val()
        }

        this.listen()
        this.reset()
    }

    Fileinput.DEFAULTS = {
        clearName: true
    }

    Fileinput.prototype.listen = function () {
        this.$input.on('change.bs.fileinput', $.proxy(this.change, this))
        $(this.$input[0].form).on('reset.bs.fileinput', $.proxy(this.reset, this))

        this.$element.find('[data-trigger="fileinput"]').on('click.bs.fileinput', $.proxy(this.trigger, this))
        this.$element.find('[data-dismiss="fileinput"]').on('click.bs.fileinput', $.proxy(this.clear, this))
    },

        Fileinput.prototype.verifySizes = function (files) {
            if (typeof this.options.maxSize === 'undefined') return true

            var max = parseFloat(this.options.maxSize)
            if (max !== this.options.maxSize) return true

            for (var i = 0; i < files.length; i++) {
                var size = typeof files[i].size !== 'undefined' ? files[i].size : null
                if (size === null) continue

                size = size / 1000 / 1000 /* convert from bytes to MB */
                if (size > max) return false
            }

            return true
        }

    Fileinput.prototype.change = function (e) {
        var files = e.target.files === undefined ? (e.target && e.target.value ? [{name: e.target.value.replace(/^.+\\/, '')}] : []) : e.target.files

        e.stopPropagation()

        if (files.length === 0) {
            this.clear()
            this.$element.trigger('clear.bs.fileinput')
            return
        }

        if (!this.verifySizes(files)) {
            this.$element.trigger('max_size.bs.fileinput')

            this.clear()
            this.$element.trigger('clear.bs.fileinput')
            return
        }

        this.$hidden.val('')
        this.$hidden.attr('name', '')
        this.$input.attr('name', this.name)

        var file = files[0]

        if (this.$preview.length > 0 && (typeof file.type !== "undefined" ? file.type.match(/^image\/(gif|png|jpeg|svg\+xml)$/) : file.name.match(/\.(gif|png|jpe?g|svg)$/i)) && typeof FileReader !== "undefined") {
            var Fileinput = this
            var reader = new FileReader()
            var preview = this.$preview
            var element = this.$element

            reader.onload = function (re) {
                var $img = $('<img>')
                $img[0].src = re.target.result
                files[0].result = re.target.result

                element.find('.fileinput-filename').text(file.name)

                // if parent has max-height, using `(max-)height: 100%` on child doesn't take padding and border into account
                if (preview.css('max-height') != 'none') {
                    var mh = parseInt(preview.css('max-height'), 10) || 0
                    var pt = parseInt(preview.css('padding-top'), 10) || 0
                    var pb = parseInt(preview.css('padding-bottom'), 10) || 0
                    var bt = parseInt(preview.css('border-top'), 10) || 0
                    var bb = parseInt(preview.css('border-bottom'), 10) || 0

                    $img.css('max-height', mh - pt - pb - bt - bb)
                }

                preview.html($img)
                if (Fileinput.options.exif) {
                    //Fix image tranformation if this is possible
                    Fileinput.setImageTransform($img, file);
                }
                element.addClass('fileinput-exists').removeClass('fileinput-new')

                element.trigger('change.bs.fileinput', files)
            }

            reader.readAsDataURL(file)
        } else {
            var text = file.name
            var $nameView = this.$element.find('.fileinput-filename')

            if (files.length > 1) {
                text = $.map(files, function (file) {
                    return file.name;
                }).join(', ')
            }

            $nameView.text(text)
            this.$preview.text(file.name)
            this.$element.addClass('fileinput-exists').removeClass('fileinput-new')
            this.$element.trigger('change.bs.fileinput')
        }
    },

        Fileinput.prototype.setImageTransform = function ($img, file) {
            var Fileinput = this;
            var reader = new FileReader();
            reader.onload = function (me) {
                var transform = false;
                var view = new DataView(reader.result);
                var exif = Fileinput.getImageExif(view);
                if (exif) {
                    Fileinput.resetOrientation($img, exif);
                }
            }

            reader.readAsArrayBuffer(file);
        }

    Fileinput.prototype.getImageExif = function (view) {
        if (view.getUint16(0, false) != 0xFFD8) {
            return -2;
        }
        var length = view.byteLength, offset = 2;
        while (offset < length) {
            var marker = view.getUint16(offset, false);
            offset += 2;
            if (marker == 0xFFE1) {
                if (view.getUint32(offset += 2, false) != 0x45786966) {
                    return -1;
                }
                var little = view.getUint16(offset += 6, false) == 0x4949;
                offset += view.getUint32(offset + 4, little);
                var tags = view.getUint16(offset, little);
                offset += 2;
                for (var i = 0; i < tags; i++) {
                    if (view.getUint16(offset + (i * 12), little) == 0x0112) {
                        return view.getUint16(offset + (i * 12) + 8, little);
                    }
                }
            } else if ((marker & 0xFF00) != 0xFF00) {
                break;
            } else {
                offset += view.getUint16(offset, false);
            }
        }

        return -1;
    }

    Fileinput.prototype.resetOrientation = function ($img, transform) {
        var img = new Image();

        img.onload = function () {
            var width = img.width,
                height = img.height,
                canvas = document.createElement('canvas'),
                ctx = canvas.getContext("2d");

            // set proper canvas dimensions before transform & export
            if ([5, 6, 7, 8].indexOf(transform) > -1) {
                canvas.width = height;
                canvas.height = width;
            } else {
                canvas.width = width;
                canvas.height = height;
            }

            // transform context before drawing image
            switch (transform) {
                case 2:
                    ctx.transform(-1, 0, 0, 1, width, 0);
                    break;
                case 3:
                    ctx.transform(-1, 0, 0, -1, width, height);
                    break;
                case 4:
                    ctx.transform(1, 0, 0, -1, 0, height);
                    break;
                case 5:
                    ctx.transform(0, 1, 1, 0, 0, 0);
                    break;
                case 6:
                    ctx.transform(0, 1, -1, 0, height, 0);
                    break;
                case 7:
                    ctx.transform(0, -1, -1, 0, height, width);
                    break;
                case 8:
                    ctx.transform(0, -1, 1, 0, 0, width);
                    break;
                default:
                    ctx.transform(1, 0, 0, 1, 0, 0);
            }

            // draw image
            ctx.drawImage(img, 0, 0);

            // export base64
            $img.attr('src', canvas.toDataURL());
        };

        img.src = $img.attr('src');
    };

    Fileinput.prototype.clear = function (e) {
        if (e) e.preventDefault()

        this.$hidden.val('')
        this.$hidden.attr('name', this.name)
        if (this.options.clearName) this.$input.attr('name', '')

        //ie8+ doesn't support changing the value of input with type=file so clone instead
        if (isIE) {
            var inputClone = this.$input.clone(true);
            this.$input.after(inputClone);
            this.$input.remove();
            this.$input = inputClone;
        } else {
            this.$input.val('')
        }

        this.$preview.html('')
        this.$element.find('.fileinput-filename').text('')
        this.$element.addClass('fileinput-new').removeClass('fileinput-exists')

        if (e !== undefined) {
            this.$input.trigger('change')
            this.$element.trigger('clear.bs.fileinput')
        }
    },

        Fileinput.prototype.reset = function () {
            this.clear()

            this.$hidden.val(this.original.hiddenVal)
            this.$preview.html(this.original.preview)
            this.$element.find('.fileinput-filename').text('')

            if (this.original.exists) this.$element.addClass('fileinput-exists').removeClass('fileinput-new')
            else this.$element.addClass('fileinput-new').removeClass('fileinput-exists')

            this.$element.trigger('reseted.bs.fileinput')
        },

        Fileinput.prototype.trigger = function (e) {
            this.$input.trigger('click')
            e.preventDefault()
        }


    // FILEUPLOAD PLUGIN DEFINITION
    // ===========================

    var old = $.fn.fileinput

    $.fn.fileinput = function (options) {
        return this.each(function () {
            var $this = $(this),
                data = $this.data('bs.fileinput')
            if (!data) $this.data('bs.fileinput', (data = new Fileinput(this, options)))
            if (typeof options == 'string') data[options]()
        })
    }

    $.fn.fileinput.Constructor = Fileinput


    // FILEINPUT NO CONFLICT
    // ====================

    $.fn.fileinput.noConflict = function () {
        $.fn.fileinput = old
        return this
    }


    // FILEUPLOAD DATA-API
    // ==================

    $(document).on('click.fileinput.data-api', '[data-provides="fileinput"]', function (e) {
        var $this = $(this)
        if ($this.data('bs.fileinput')) return
        $this.fileinput($this.data())

        var $target = $(e.target).closest('[data-dismiss="fileinput"],[data-trigger="fileinput"]');
        if ($target.length > 0) {
            e.preventDefault()
            $target.trigger('click.bs.fileinput')
        }
    })

}(window.jQuery);