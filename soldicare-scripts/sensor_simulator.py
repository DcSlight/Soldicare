import requests
import random
import time
import json
import logging
from datetime import datetime, timezone

# ========== CONFIG ==========
BASE_URL = "http://localhost:8084/ambient-intelligence"
OBJECTS_URL = f"{BASE_URL}/objects"
COMMANDS_URL = f"{BASE_URL}/commands"
LOG_FILE = "sensor_simulator.log"

SENSOR_SYSTEM_ID = "2025b.Itay.Chabra"
SENSOR_EMAIL = "joanna@demo.org"

COMMAND_SYSTEM_ID = "2025b.Itay.Chabra"
COMMAND_EMAIL = "jack@demo.org"

CRITICAL_THRESHOLDS = {
    "Temperature": 39.0,
    "HeartRate": 110,
    "BloodOxygen": 92,
    "BloodPressure": 135,
    "Hydration": 60
}

# ========== LOGGER ==========
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
    handlers=[
        logging.FileHandler(LOG_FILE),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger("SensorSimulator")


# ========== FUNCTIONS ==========

def fetch_all_soldiers():
    url = (
        f"{OBJECTS_URL}/search/byType/soldier"
        f"?userSystemID={SENSOR_SYSTEM_ID}&userEmail={SENSOR_EMAIL.replace('@', '%40')}"
        f"&size=100&page=0"
    )
    try:
        logger.info("Fetching all soldiers...")
        res = requests.get(url, headers={"accept": "application/json"})
        if res.status_code == 200:
            data = res.json()
            return [
                {
                    "id": item["id"]["objectId"],
                    "name": item["objectDetails"].get("name", "Unknown"),
                    "serial": item["objectDetails"].get("serial", "Unknown")
                }
                for item in data
            ]
        else:
            logger.error(f"Failed to fetch soldiers: {res.status_code} - {res.text}")
    except Exception as e:
        logger.error(f"Exception while fetching soldiers: {e}")
    return []


def fetch_soldier_unit_name(soldier_id):
    url = (
        f"{OBJECTS_URL}/{SENSOR_SYSTEM_ID}/{soldier_id}/parents"
        f"?userSystemID={SENSOR_SYSTEM_ID}&userEmail={SENSOR_EMAIL.replace('@', '%40')}"
        f"&size=10&page=0"
    )
    try:
        res = requests.get(url)
        if res.status_code == 200:
            data = res.json()
            for item in data:
                if item.get("type") == "armyUnit":
                    return item.get("objectDetails", {}).get("name", "UnknownUnit")
            logger.warning(f"No armyUnit found for soldier {soldier_id}")
        else:
            logger.warning(f"Failed to fetch unit for {soldier_id}: {res.status_code}")
    except Exception as e:
        logger.error(f"Exception while fetching unit for {soldier_id}: {e}")
    return "UnknownUnit"


def generate_random_sensor_data():
    return {
        "type": "sensor",
        "alias": "allSensor",
        "status": "ACTIVE",
        "active": True,
        "createdBy": {
            "userId": {
                "email": SENSOR_EMAIL,
                "systemID": SENSOR_SYSTEM_ID
            }
        },
        "objectDetails": {
            "Temperature": f"{random.uniform(35.0, 42.0):.1f}",
            "HeartRate": f"{random.randint(60, 130)}",
            "BloodOxygen": f"{random.randint(85, 100)}",
            "BloodPressure": f"{random.randint(90, 150)}",
            "Hydration": f"{random.randint(40, 100)}",
            "ECG": random.choice(["Normal", "Irregular"]),
            "EEG": random.choice(["Stable", "Unstable"]),
            "location": {
                "lat": f"{random.uniform(-90.0, 90.0):.6f}",
                "lng": f"{random.uniform(-180.0, 180.0):.6f}"
            }
        }
    }


def check_critical_values(sensor_data):
    critical = {}
    for key, threshold in CRITICAL_THRESHOLDS.items():
        val = float(sensor_data["objectDetails"][key])
        if val > threshold:
            critical[key] = str(val)
    return critical


def send_command(critical_values, sensor_id, soldier_id, soldier_name, soldier_unit_name, soldier_serial):
    command_data = {
        "command": "ALERT_CRITICAL_VALUES",
        "targetObject": {
            "id": {
                "objectId": sensor_id,
                "systemID": SENSOR_SYSTEM_ID
            }
        },
        "invocationTimestamp": datetime.now(timezone.utc).isoformat(),
        "invokedBy": {
            "userId": {
                "email": COMMAND_EMAIL,
                "systemID": COMMAND_SYSTEM_ID
            }
        },
        "commandAttributes": {
            **critical_values,
            "relatedSoldier": {
                "id": {
                    "objectId": soldier_id,
                    "systemID": SENSOR_SYSTEM_ID
                },
                "name": soldier_name,
                "unitName": soldier_unit_name,
                "serial": soldier_serial
            }
        }
    }

    logger.info(f"Sending POST to {COMMANDS_URL}")
    logger.debug(f"Command Payload:\n{json.dumps(command_data, indent=2)}")

    try:
        response = requests.post(COMMANDS_URL, json=command_data)
        logger.info(f"Command response status: {response.status_code}")
        if response.status_code >= 400:
            logger.warning(f"Command failed. Response: {response.text}")
    except Exception as e:
        logger.error(f"Exception during command send: {e}")


def bind_sensor_to_soldier(sensor_id, soldier_id):
    url = (
        f"{OBJECTS_URL}/{SENSOR_SYSTEM_ID}/{soldier_id}/children"
        f"?userSystemID={SENSOR_SYSTEM_ID}&userEmail={SENSOR_EMAIL.replace('@', '%40')}"
    )
    payload = {
        "childId": {
            "objectId": sensor_id,
            "systemID": SENSOR_SYSTEM_ID
        }
    }

    logger.info(f"Binding sensor {sensor_id} to soldier {soldier_id}")
    try:
        response = requests.put(url, json=payload)
        logger.info(f"Bind response status: {response.status_code}")
        if response.status_code >= 400:
            logger.warning(f"Bind failed: {response.text}")
    except Exception as e:
        logger.error(f"Exception during bind: {e}")


# ========== MAIN LOOP ==========

if __name__ == "__main__":
    soldiers_list = fetch_all_soldiers()
    if not soldiers_list:
        logger.error("No soldiers found. Exiting.")
        exit(1)

    logger.info(f"Fetched {len(soldiers_list)} soldiers.")

    while True:
        soldier = random.choice(soldiers_list)
        soldier_id = soldier["id"]
        soldier_name = soldier["name"]
        soldier_serial = soldier["serial"]
        soldier_unit_name = fetch_soldier_unit_name(soldier_id)

        sensor_data = generate_random_sensor_data()

        logger.info(f"Sending POST to {OBJECTS_URL}")
        logger.debug(f"Sensor Payload:\n{json.dumps(sensor_data, indent=2)}")

        try:
            res = requests.post(OBJECTS_URL, json=sensor_data)
            logger.info(f"Sensor response status: {res.status_code}")
            if res.status_code >= 400:
                logger.warning(f"Sensor data failed. Response: {res.text}")
                time.sleep(5)
                continue

            response_data = res.json()
            sensor_id = response_data["id"]["objectId"]
            logger.info(f"Received sensor objectId: {sensor_id}")

            bind_sensor_to_soldier(sensor_id, soldier_id)

            critical = check_critical_values(sensor_data)
            if critical:
                logger.warning(f"Critical values detected from sensor {sensor_id}: {critical}")
                send_command(critical, sensor_id, soldier_id, soldier_name, soldier_unit_name, soldier_serial)

        except Exception as e:
            logger.error(f"Exception during sensor simulation: {e}")

        time.sleep(5)
