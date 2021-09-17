{
  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, flake-utils }:
  flake-utils.lib.eachDefaultSystem
  (system:
  let
    pkgs = import nixpkgs {
      system = "${system}";
      config.allowUnfree = true;
    };
  in {
    devShell =
      (pkgs.buildFHSUserEnv {
        name = "android-sdk-environment";
        targetPkgs = pkgs: ([
          pkgs.jdk8
          pkgs.android-studio
          pkgs.glibc
          pkgs.coreutils
          pkgs.ncurses5
          pkgs.qemu
          pkgs.xorg.libX11
          pkgs.libpulseaudio
          pkgs.alsa-lib
          pkgs.bash
          pkgs.cacert
          pkgs.dbus
          pkgs.direnv
          pkgs.expat
          pkgs.findutils
          pkgs.file
          pkgs.fish
          pkgs.git
          pkgs.glxinfo
          pkgs.gnugrep
          pkgs.gnused
          pkgs.gnutar
          pkgs.gtk2
          pkgs.glib
          pkgs.gzip
          pkgs.fontconfig
          pkgs.freetype
          pkgs.libGL
          pkgs.libuuid
          pkgs.xorg.libxcb
          pkgs.xorg.libXcomposite
          pkgs.xorg.libXcursor
          pkgs.xorg.libXdamage
          pkgs.xorg.libXext
          pkgs.xorg.libXfixes
          pkgs.xorg.libXi
          pkgs.xorg.libXrandr
          pkgs.xorg.libXrender
          pkgs.xorg.libXtst
          pkgs.makeWrapper
          pkgs.maven
          pkgs.neovim
          pkgs.nspr
          pkgs.nss
          pkgs.pciutils
          pkgs.ps
          pkgs.ripgrep
          pkgs.xorg.setxkbmap
          pkgs.systemd
          pkgs.trash-cli
          pkgs.unzip
          pkgs.which
          pkgs.xkeyboard_config
          pkgs.zlib
          ]);
          runScript = ''
            bash -c 'export PATH=$HOME/Android/Sdk/platform-tools:$PATH &&
            export PATH=$HOME/action-based-test-carving/scripts:$PATH &&
            export ANDROID_SDK_ROOT=~/Android/Sdk &&
            export ANDROID_HOME=$ANDROID_SDK_ROOT &&
            export _JAVA_AWT_WM_NONREPARENTING=1 &&
            fish'
          '';
        }).env;
      });
    }

