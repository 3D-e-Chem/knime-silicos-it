# Build

The Windows executables have been build on a Windows 10 Virtualbox Virtual Machine downloaded from https://developer.microsoft.com/en-us/microsoft-edge/tools/vms/

The following steps where performed inside the VM.
The first time the VM is started, the cpu load is very high due to Windows initialization (Windows updates etc.), so wait for the load to drop before continueing.

## Open Babel installation

On http://openbabel.org/wiki/Category:Installation download the 64bit Windows installer.
Install it in C:\OpenBabel-x.x.x

Also download the source and extract it to c:\openbabel-x.x.x-source

## Installation prerequisites

Install Chocolatery by following instructions at https://chocolatey.org/install

```
choco install 7zip cmake VisualStudio2017Community VisualStudio2017buildtools visualcoo-build-tools
ms
```

Start Visual Studio
    * Skip login
    * Choose `Visual C++` as development settings

## Silicos-it compilation

The source tarballs from http://silicos-it.be.s3-website-eu-west-1.amazonaws.com/software/software.html .

### filter-it

1. Download and extract source tarball.
2. Create `build/` dir, will be used to build binaries in
3. Open `Developer Command Prompt for VS 2017`
3. Run `c:\Program Files\CMake\bin\cmake-gui`
    * Select source dir
    * Select build dir
    * Press Generate Button
    * Select `Visual Studio 15 2017 Win64` as generator
    * Set OPENBABEL2_INCLUDE_DIRS to c:\openbabel-x.x.x-source/include/openbabel
    * Set OPENBABEL2_LIBRARIES to c:\OpenBabel-x.x.x/openbabel-2.dll
    * Press Configure
    * Press Generate
    * Press Open project
    * Install the required items
    * Build the ALL_BUILD solution project

Stuck HERE: Due to lots of compile errors.

## Capture Silicos-it software

The executables, libraries and datasets where copied out of the VM using ssh.

# Content

Contents of `src/main/resources/`:

* bin/align-it.exe
* bin/shape-it.exe
* bin/strip-it.exe
* bin/filter-it.exe
* data/ringtyp.txt, and lots of others files in data/
* lib/plugins/svgformat.ddl and lots of other *.ddl files in lib/plugins/
* lib/libopenbabel.ddl
