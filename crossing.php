<?php

/**
 * Created by PhpStorm.
 * User: mmarti
 * Date: 20.11.2015
 * Time: 11:54
 */

require_once('connection/DBConfig.php');
require_once('connection/DBCrossing.php');

function crossingPoints($bounds, $maxCrossingAmount) {
    $bounds = explode(",", $bounds);

    for ($i = 0; $i < 4; $i++) {
        if (!is_numeric($bounds[$i])) {
            return array("error" => 404, "reason" => 'Parameter "bounds" has an invalid value');
        }
    }

    if (count($bounds) != 4) {
        return array("error" => 404, "reason" => 'Parameter "bounds" has an invalid amount of parameters');
    }

    if (!is_numeric($maxCrossingAmount)) {
        return array("error" => 404, "reason" => 'Parameter "maxamount" has an invalid value');
    }

    $crossingConnection = new DBCrossing();
    $query = $crossingConnection->getAllCrossings(getSnap($maxCrossingAmount, $crossingConnection), $bounds);

    $crossings = array(
        "type" => "FeatureCollection"
    );

    while ($row = pg_fetch_array($query, null, PGSQL_ASSOC)) {
        $crossings['features'][] = array(
            "type" => "Feature",
            "geometry" => array(
                "type" => "Point",
                "coordinates" => [doubleval($row['x']), doubleval($row['y'])]
            ),
            "properties" => getOsmDetailsClustered($row)
        );
    }

    $crossingConnection->closeConnection();
    return $crossings;
}

function crossingDetail($osmNodeId) {
    if (!is_numeric($osmNodeId)) {
        return array("error" => 404, "reason" => 'Parameter "crosswalk" has an invalid value.');
    }

    $crossingConnection = new DBCrossing();
    $query = $crossingConnection->getCrossing($osmNodeId);
    $resultset = pg_fetch_all($query)[0];

    if (!$resultset) {
        return array("error" => 404, "reason" => 'Parameter "crosswalk" has an invalid value.');
    }

    $crossing = getOsmDetails($resultset);
    $crossing['ratings'] = getRatings($crossingConnection->getRating($resultset['id']));

    $crossingConnection->closeConnection();
    return $crossing;
}

function getOsmDetails($row) {
    return array(
        "osm_node_id" => doubleval($row['osm_node_id']),
        "status" => intval($row['status']),
        "traffic_signals" => myBoolval($row['traffic_signals']),
        "island" => myBoolval($row['island']),
        "unmarked" => myBoolval($row['unmarked']),
        "button_operated" => myBoolval($row['button_operated']),
        "sloped_curb" => $row['sloped_curb'],
        "tactile_paving" => myBoolval($row['tactile_paving']),
        "traffic_signals_vibration" => myBoolval($row['traffic_signals_vibration']),
        "traffic_signals_sound" => myBoolval($row['traffic_signals_sound'])
    );
}

function getOsmDetailsClustered($row) {
    $row['osm_node_id'] = str_replace('{', '', str_replace('}', '', $row['osm_node_id']));
    $row['status'] = str_replace('{', '', str_replace('}', '', $row['status']));
    $row['traffic_signals'] = str_replace('{', '', str_replace('}', '', $row['traffic_signals']));
    $row['island'] = str_replace('{', '', str_replace('}', '', $row['island']));
    $row['unmarked'] = str_replace('{', '', str_replace('}', '', $row['unmarked']));   
    $row['button_operated'] = str_replace('{', '', str_replace('}', '', $row['button_operated']));
    $row['sloped_curb'] = str_replace('{', '', str_replace('}', '', $row['sloped_curb']));
    $row['tactile_paving'] = str_replace('{', '', str_replace('}', '', $row['tactile_paving']));
    $row['traffic_signals_vibration'] = str_replace('{', '', str_replace('}', '', $row['traffic_signals_vibration']));
    $row['traffic_signals_sound'] = str_replace('{', '', str_replace('}', '', $row['traffic_signals_sound']));

    if (strpos($row['osm_node_id'], ',') !== false) {
        return array(
            "osm_node_id" => array_map('doubleval', explode(',', $row['osm_node_id'])),
            "status" => array_map('intval', explode(',', $row['status'])),
            "traffic_signals" => array_map('myBoolval', explode(',', $row['traffic_signals'])),
            "island" => array_map('myBoolval', explode(',', $row['island'])),
            "unmarked" => array_map('myBoolval', explode(',', $row['unmarked'])),
            "button_operated" => array_map('myBoolval', explode(',', $row['button_operated'])),
            "sloped_curb" => explode(',', $row['sloped_curb']),
            "tactile_paving" => array_map('myBoolval', explode(',', $row['tactile_paving'])),
            "traffic_signals_vibration" => array_map('myBoolval', explode(',', $row['traffic_signals_vibration'])),
            "traffic_signals_sound" => array_map('myBoolval', explode(',', $row['traffic_signals_sound']))
        );
    }

    return getOsmDetails($row);
}

function getRatings($query) {
    while ($row = pg_fetch_array($query, null, PGSQL_ASSOC)) {
        $ratings[] = array(
            "spatial_clarity" => $row['sc_value'],
            "illumination" => $row['i_value'],
            "traffic" => $row['t_value'],
            "image_weblink" => $row['image_weblink'],
            "comment" => $row['comment'],
            "username" => $row['name']
        );
    }

    return $ratings;
}

function myBoolval($var) {
    if ($var == "f") {
        return false;
    }

    return boolval($var);
}

//$crossingConnection = new DBCrossing();
//echo getSnap(1, $crossingConnection);
//$crossingConnection->closeConnection();

function getSnap($maxAmount, $crossingConnection) {
    $numbers = range(-1000, 733000, 1000);
    $position = halve(count($numbers));
    $maxHeight = count($numbers);
    $minHeight = 0;

    while (true) {
        $oldMinHeight = $minHeight;
        $oldMaxHeight = $maxHeight;
        $query = $crossingConnection->getClusteredAmount($numbers[$position]);
        $queryAmount = pg_fetch_all($query)[0]['amount'];

        if ($queryAmount > $maxAmount) {
            $minHeight = $position;

            if ($maxHeight == $oldMaxHeight && $minHeight == $oldMinHeight) {
                break;
            }

            $position += halve($maxHeight - $position);

        } else if ($queryAmount < $maxAmount) {
            $maxHeight = $position;

            if ($maxHeight == $oldMaxHeight && $minHeight == $oldMinHeight) {
                break;
            }

            $position = halve($position - $minHeight);
        } else {
            break;
        }
    }

    return $numbers[$position];
}

function halve($number) {
    if ($number % 2 == 0) {
        return intval($number / 2);
    } else {
        return intval($number / 2 + 0.5);
    }
}
?>