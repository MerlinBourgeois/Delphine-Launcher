<?php
/*
 * Converts a filesystem tree to a PHP array.
 */
function getDirContents($path) {
    $rii = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($path));

    $data = array(); 
    foreach ($rii as $file){    
        if (!$file->isDir()){
            $link = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? "https" : "http") . "://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";
            $phpFileName = basename(__FILE__);
            if(strpos($link, $phpFileName) !== false){
                $link =    str_replace($phpFileName, "", $link);
            }
            $pathF = $file->getPathname();//substr(, strlen(basename($path)));
            $data[] = [ 'path' => $pathF , 'size' => filesize($file), 'downloadURL' => $link . $pathF,'sha1' => sha1 ($file)];
        }
    }
    \usort($data, function($a, $b) {
                $aa = isset($a['file']) ? $a['file'] : $a['path'];
                $bb = isset($b['file']) ? $b['file'] : $b['path'];

                return \strcmp($aa, $bb);
        });

        return $data;
}

/*
 * Converts a filesystem tree to a JSON representation.
 */
function dir_to_json($dir)
{
        $data = getDirContents($dir);
        $data = json_encode($data, JSON_PRETTY_PRINT);

        return str_replace("\\", "", $data);;
}

echo '{ "extfiles":' . dir_to_json('extfiles') . '}';